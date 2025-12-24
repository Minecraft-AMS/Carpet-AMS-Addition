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
import carpetamsaddition.commands.suggestionProviders.SetSuggestionProvider;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.config.rule.commandLeader.LeaderConfig;
import carpetamsaddition.commands.rule.commandWhere.WhereCommandRegistry;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.PlayerUtil;

import com.google.common.collect.ImmutableSet;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class LeaderCommandRegistry {
    private static final Translator tr = new Translator("command.leader");
    // private static final String MSG_HEAD = "<commandPlayerLeader> ";
    private static final Set<Integer> suggestionIntervalOptions = ImmutableSet.of(20, 40, 80, 160, 320, 640, -1024);
    private static final Map<UUID, Integer> PLAYER_TICK_INTERVAL = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> PLAYER_TICK_COUNTER = new ConcurrentHashMap<>();
    public static final MobEffectInstance HIGH_LIGHT = new MobEffectInstance(MobEffects.GLOWING, MobEffectInstance.INFINITE_DURATION);
    public static final Map<String, UUID> LEADER_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("leader")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandPlayerLeader))

            // add
            .then(literal("add")
            .then(argument("player", EntityArgument.player())
            .executes(c -> add(c.getSource().getServer(), EntityArgument.getPlayer(c, "player")))))

            // remove
            .then(literal("remove")
            .then(argument("player", EntityArgument.player())
            .executes(c -> remove(c.getSource().getServer(), EntityArgument.getPlayer(c, "player")))))

            // removeAll
            .then(literal("removeAll")
            .executes(c -> removeAll(c.getSource().getServer(), c.getSource())))

            // list
            .then(literal("list")
            .executes(c -> list(c.getSource(), c.getSource().getPlayer())))

            // help
            .then(literal("help")
            .executes(c -> help(c.getSource())))

            // broadcastLeaderPos
            .then(literal("broadcastLeaderPos")
            .then(argument("player", EntityArgument.player())
            .then(literal("interval")
            .then(argument("interval", IntegerArgumentType.integer()).suggests(new SetSuggestionProvider<>(suggestionIntervalOptions))
            .executes(context -> broadcastPosTickInterval(
                EntityArgument.getPlayer(context, "player"),
                context.getSource().getServer(),
                IntegerArgumentType.getInteger(context, "interval")
            ))))))
        );
    }

    public static int broadcastPosTickInterval(Player targetPlayer, MinecraftServer server, int interval) {
        if (!LEADER_MAP.containsKey(PlayerUtil.getName(targetPlayer))) {
            Messenger.sendServerMessage(
                server, Messenger.f(tr.tr("is_not_leader", PlayerUtil.getName(targetPlayer)), ChatFormatting.RED, ChatFormatting.ITALIC)
            );

            return 0;
        }

        UUID playerUUID = PlayerUtil.getPlayerUUID(targetPlayer);
        PLAYER_TICK_INTERVAL.put(playerUUID, interval);
        PLAYER_TICK_COUNTER.put(playerUUID, 0);

        // 立马先发一遍
        if (canBroadcastPos(targetPlayer)) {
            WhereCommandRegistry.sendMessage(targetPlayer);
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
                        WhereCommandRegistry.sendMessage(player);
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
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("add", PlayerUtil.getName(targetPlayer)), ChatFormatting.GRAY));
            LEADER_MAP.put(PlayerUtil.getName(targetPlayer), PlayerUtil.getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("is_already_leader", PlayerUtil.getName(targetPlayer)), ChatFormatting.RED, ChatFormatting.ITALIC));
        }

        return 1;
    }

    private static int remove(MinecraftServer server, Player targetPlayer) {
        if (LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(targetPlayer))) {
            targetPlayer.removeEffect(HIGH_LIGHT.getEffect());
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("remove", PlayerUtil.getName(targetPlayer)), ChatFormatting.GRAY));
            LEADER_MAP.remove(PlayerUtil.getName(targetPlayer), PlayerUtil.getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("is_not_leader", PlayerUtil.getName(targetPlayer)), ChatFormatting.RED, ChatFormatting.ITALIC));
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
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), ChatFormatting.YELLOW));
        return 1;
    }

    private static int list(CommandSourceStack source, Player player) {
        Messenger.tell(source, Messenger.f(
            Messenger.c(
                tr.tr("list_title"),
                Messenger.endl(),
                Messenger.dline()
            ), ChatFormatting.AQUA, ChatFormatting.BOLD)
        );

        for (Map.Entry<String, UUID> entry : LEADER_MAP.entrySet()) {
            String playerName = entry.getKey();
            UUID playerUUID = PlayerUtil.getPlayerUUID(player);
            Messenger.tell(source, Messenger.f(Messenger.s(playerName + " - " + playerUUID), ChatFormatting.DARK_AQUA));
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
        ), ChatFormatting.GRAY));

        return 1;
    }

    public static void onPlayerLoggedIn(Player player) {
        if (
            player.getActiveEffectsMap().containsKey(LeaderCommandRegistry.HIGH_LIGHT.getEffect()) &&
            !LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player)) &&
            !LEADER_MAP.containsKey(PlayerUtil.getName(player))
        ) {
            player.removeEffect(LeaderCommandRegistry.HIGH_LIGHT.getEffect());
        }

        if (LeaderCommandRegistry.LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player))) {
            player.addEffect(LeaderCommandRegistry.HIGH_LIGHT, player);
        }
    }

    private static void saveToJson() {
        LeaderConfig.getInstance().saveToJson(LEADER_MAP);
    }
}
