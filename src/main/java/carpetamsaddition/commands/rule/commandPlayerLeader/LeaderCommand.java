/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.commands.rule.commandPlayerLeader;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.api.command.CommandPath;
import carpetamsaddition.api.command.suggestionProviders.SetSuggestionProvider;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.*;
import carpetamsaddition.config.rule.commandLeader.LeaderConfig;
import carpetamsaddition.commands.rule.commandWhere.WhereCommand;

import carpetamsaddition.utils.messenger.Messenger;
import com.google.common.collect.ImmutableSet;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LeaderCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.leader");
    private static final int GLOWING_TIME = (int) Double.POSITIVE_INFINITY;
    private static final Set<Integer> suggestionIntervalOptions = ImmutableSet.of(20, 40, 80, 160, 320, 640, -1024);
    private static final Map<UUID, Integer> PLAYER_TICK_INTERVAL = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> PLAYER_TICK_COUNTER = new ConcurrentHashMap<>();
    public static final MobEffectInstance HIGH_LIGHT = new MobEffectInstance(MobEffects.GLOWING, GLOWING_TIME);
    public static final Map<String, UUID> LEADER_MAP = new ConcurrentHashMap<>();

    @Override
    public void define(CommandBuilder command) {
        command.name("leader")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandPlayerLeader)
            .route("add", Arguments.player("player")).executes(c -> add(c.server(), c.get(Arguments.player("player"))))
            .route("remove", Arguments.player("player")).executes(c -> remove(c.server(), c.get(Arguments.player("player"))))
            .route("removeAll").executes(c -> removeAll(c.server(), c.source()))
            .route("list").executes(c -> list(c.source(), c.source().getPlayer()))
            .route("help").executes(c -> help(c.source()))
            .route(CommandPath.path().literal("broadcastLeaderPos").argument(Arguments.player("player")).literal("interval").argument(Arguments.suggests(Arguments.integer("interval"), new SetSuggestionProvider<>(suggestionIntervalOptions))))
            .executes(c -> broadcastPosTickInterval(c.get(Arguments.player("player")), c.server(), c.get(Arguments.integer("interval"))));
    }

    public static int broadcastPosTickInterval(Player targetPlayer, MinecraftServer server, int interval) {
        if (!LEADER_MAP.containsKey(PlayerUtil.getName(targetPlayer))) {
            Messenger.sendServerMessage(
                server, Messenger.f(tr.tr("is_not_leader", PlayerUtil.getName(targetPlayer)), Layout.RED, Layout.ITALIC)
            );

            return 0;
        }

        UUID playerUUID = PlayerUtil.getPlayerUUID(targetPlayer);
        PLAYER_TICK_INTERVAL.put(playerUUID, interval);
        PLAYER_TICK_COUNTER.put(playerUUID, 0);

        // 立马先发一遍
        if (canBroadcastPos(targetPlayer)) {
            WhereCommand.sendMessage(targetPlayer);
        }

        return 1;
    }

    public static void tick() {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandPlayerLeader, "false") && !PLAYER_TICK_INTERVAL.isEmpty() && !PLAYER_TICK_COUNTER.isEmpty()) {
            // 存储需要移除的玩家UUID
            List<UUID> needRemovePlayer = new ArrayList<>();
            for (Map.Entry<UUID, Integer> entry : PLAYER_TICK_INTERVAL.entrySet()) {
                UUID playerUUID = entry.getKey();
                int interval = entry.getValue();
                if (interval <= -1 || !LEADER_MAP.containsValue(playerUUID)) {
                    needRemovePlayer.add(playerUUID);
                }
            }

            // 统一删除
            needRemovePlayer.forEach(uuid -> {
                PLAYER_TICK_INTERVAL.remove(uuid);
                PLAYER_TICK_COUNTER.remove(uuid);
            });

            // 继续处理广播
            for (Map.Entry<UUID, Integer> entry : PLAYER_TICK_INTERVAL.entrySet()) {
                UUID playerUUID = entry.getKey();
                int interval = entry.getValue();
                int tickCounter = PLAYER_TICK_COUNTER.getOrDefault(playerUUID, 0);
                tickCounter++;
                if (tickCounter >= interval && MinecraftServerUtil.serverIsRunning()) {
                    Player player = MinecraftServerUtil.getServer().getPlayerList().getPlayer(playerUUID);
                    if (canBroadcastPos(player)) {
                        WhereCommand.sendMessage(player);
                    }
                    PLAYER_TICK_COUNTER.put(playerUUID, 0);
                } else {
                    PLAYER_TICK_COUNTER.put(playerUUID, tickCounter);
                }
            }
        }
    }

    private static boolean canBroadcastPos(Player player) {
        return
            player != null &&
            player.isAlive() &&
            LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player)) &&
            LEADER_MAP.containsKey(PlayerUtil.getName(player));
    }

    private static int add(MinecraftServer server, Player targetPlayer) {
        if (!LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(targetPlayer))) {
            targetPlayer.addEffect(HIGH_LIGHT);
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("add", PlayerUtil.getName(targetPlayer)), Layout.GRAY));
            LEADER_MAP.put(PlayerUtil.getName(targetPlayer), PlayerUtil.getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("is_already_leader", PlayerUtil.getName(targetPlayer)), Layout.RED, Layout.ITALIC));
        }

        return 1;
    }

    private static int remove(MinecraftServer server, Player targetPlayer) {
        if (LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(targetPlayer))) {
            targetPlayer.removeEffect(HIGH_LIGHT.getEffect());
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("remove", PlayerUtil.getName(targetPlayer)), Layout.GRAY));
            LEADER_MAP.remove(PlayerUtil.getName(targetPlayer), PlayerUtil.getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("is_not_leader", PlayerUtil.getName(targetPlayer)), Layout.RED, Layout.ITALIC));
        }
        return 1;
    }

    private static int removeAll(MinecraftServer server, CommandSourceStack source) {
        Iterator<Map.Entry<String, UUID>> iterator = LEADER_MAP.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, UUID> entry = iterator.next();
            UUID playerUUID = entry.getValue();
            Player targetPlayer = server.getPlayerList().getPlayer(playerUUID);

            if (targetPlayer != null) {
                targetPlayer.removeEffect(HIGH_LIGHT.getEffect());
            }

            iterator.remove();
        }

        LEADER_MAP.clear();
        saveToJson();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.YELLOW));
        return 1;
    }

    private static int list(CommandSourceStack source, Player player) {
        Messenger.tell(source, Messenger.f(
            Messenger.c(
                tr.tr("list_title"),
                Messenger.endl(),
                Messenger.dline()
            ), Layout.AQUA, Layout.BOLD)
        );

        for (Map.Entry<String, UUID> entry : LEADER_MAP.entrySet()) {
            String playerName = entry.getKey();
            UUID playerUUID = PlayerUtil.getPlayerUUID(player);
            Messenger.tell(source, Messenger.f(Messenger.s(playerName + " - " + playerUUID), Layout.DARK_AQUA));
        }

        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(Messenger.c(
            tr.tr("help.add"), Messenger.endl(),
            tr.tr("help.remove"), Messenger.endl(),
            tr.tr("help.removeAll"), Messenger.endl(),
            tr.tr("help.broadcast_leader_pos"), Messenger.endl(),
            tr.tr("help.list"), Messenger.endl()
        ), Layout.GRAY));

        return 1;
    }

    public static void onPlayerLoggedIn(Player player) {
        if (
            player.getActiveEffectsMap().containsKey(LeaderCommand.HIGH_LIGHT.getEffect()) &&
            !LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player)) &&
            !LEADER_MAP.containsKey(PlayerUtil.getName(player))
        ) {
            player.removeEffect(LeaderCommand.HIGH_LIGHT.getEffect());
        }

        if (LeaderCommand.LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player))) {
            player.addEffect(
                LeaderCommand.HIGH_LIGHT
                //#if MC>=11700
                , player
                //#endif
            );
        }
    }

    private static void saveToJson() {
        LeaderConfig.getInstance().saveToJson(LEADER_MAP);
    }
}
