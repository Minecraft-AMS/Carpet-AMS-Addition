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

package club.mcams.carpet.commands.rule.commandPlayerLeader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.commands.suggestionProviders.SetSuggestionProvider;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.*;
import club.mcams.carpet.config.rule.commandLeader.LeaderConfig;
import club.mcams.carpet.commands.rule.commandWhere.WhereCommandRegistry;

import com.google.common.collect.ImmutableSet;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LeaderCommandRegistry {
    private static final Translator tr = new Translator("command.leader");
    private static final int GLOWING_TIME = Integer.MAX_VALUE;
    private static final Set<Integer> suggestionIntervalOptions = ImmutableSet.of(20, 40, 80, 160, 320, 640, -1024);
    private static final Map<UUID, Integer> PLAYER_TICK_INTERVAL = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> PLAYER_TICK_COUNTER = new ConcurrentHashMap<>();
    public static final StatusEffectInstance HIGH_LIGHT = new StatusEffectInstance(StatusEffects.GLOWING, GLOWING_TIME);
    public static final Map<String, UUID> LEADER_MAP = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("leader")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandPlayerLeader))
            .then(literal("add")
            .then(argument("player", EntityArgumentType.player())
            .executes(context -> add(context.getSource().getServer(), EntityArgumentType.getPlayer(context, "player")))))
            .then(literal("remove")
            .then(argument("player", EntityArgumentType.player())
            .executes(context -> remove(context.getSource().getServer(), EntityArgumentType.getPlayer(context, "player")))))
            .then(literal("removeAll")
            .executes(context -> removeAll(context.getSource().getServer(), context.getSource())))
            .then(literal("list")
            .executes(context -> list(context.getSource(), context.getSource().getPlayer())))
            .then(literal("help")
            .executes(context -> help(context.getSource())))
            .then(literal("broadcastLeaderPos")
            .then(argument("player", EntityArgumentType.player())
            .then(literal("interval")
            .then(argument("interval", IntegerArgumentType.integer()).suggests(new SetSuggestionProvider<>(suggestionIntervalOptions))
            .executes(context -> broadcastPosTickInterval(
                EntityArgumentType.getPlayer(context, "player"),
                context.getSource().getServer(),
                IntegerArgumentType.getInteger(context, "interval")
            ))))))
        );
    }

    public static int broadcastPosTickInterval(PlayerEntity targetPlayer, MinecraftServer server, int interval) {
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
            WhereCommandRegistry.sendMessage(targetPlayer);
        }

        return 1;
    }

    public static void tick() {
        if (!Objects.equals(AmsServerSettings.commandPlayerLeader, "false") && !PLAYER_TICK_INTERVAL.isEmpty() && !PLAYER_TICK_COUNTER.isEmpty()) {
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
                    PlayerEntity player = MinecraftServerUtil.getServer().getPlayerManager().getPlayer(playerUUID);
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

    private static boolean canBroadcastPos(PlayerEntity player) {
        return
            player != null &&
            player.isAlive() &&
            LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player)) &&
            LEADER_MAP.containsKey(PlayerUtil.getName(player));
    }

    private static int add(MinecraftServer server, PlayerEntity targetPlayer) {
        if (!LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(targetPlayer))) {
            targetPlayer.addStatusEffect(HIGH_LIGHT);
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("add", PlayerUtil.getName(targetPlayer)), Layout.GRAY));
            LEADER_MAP.put(PlayerUtil.getName(targetPlayer), PlayerUtil.getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("is_already_leader", PlayerUtil.getName(targetPlayer)), Layout.RED, Layout.ITALIC));
        }

        return 1;
    }

    private static int remove(MinecraftServer server, PlayerEntity targetPlayer) {
        if (LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(targetPlayer))) {
            targetPlayer.removeStatusEffect(HIGH_LIGHT.getEffectType());
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("remove", PlayerUtil.getName(targetPlayer)), Layout.GRAY));
            LEADER_MAP.remove(PlayerUtil.getName(targetPlayer), PlayerUtil.getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(server, Messenger.f(tr.tr("is_not_leader", PlayerUtil.getName(targetPlayer)), Layout.RED, Layout.ITALIC));
        }
        return 1;
    }

    private static int removeAll(MinecraftServer server, ServerCommandSource source) {
        Iterator<Map.Entry<String, UUID>> iterator = LEADER_MAP.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, UUID> entry = iterator.next();
            UUID playerUUID = entry.getValue();
            PlayerEntity targetPlayer = server.getPlayerManager().getPlayer(playerUUID);

            if (targetPlayer != null) {
                targetPlayer.removeStatusEffect(HIGH_LIGHT.getEffectType());
            }

            iterator.remove();
        }

        LEADER_MAP.clear();
        saveToJson();
        Messenger.tell(source, Messenger.f(tr.tr("removeAll"), Layout.YELLOW));
        return 1;
    }

    private static int list(ServerCommandSource source, PlayerEntity player) {
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

    private static int help(ServerCommandSource source) {
        Messenger.tell(source, Messenger.f(Messenger.c(
            tr.tr("help.add"), Messenger.endl(),
            tr.tr("help.remove"), Messenger.endl(),
            tr.tr("help.removeAll"), Messenger.endl(),
            tr.tr("help.broadcast_leader_pos"), Messenger.endl(),
            tr.tr("help.list"), Messenger.endl()
        ), Layout.GRAY));

        return 1;
    }

    public static void onPlayerLoggedIn(PlayerEntity player) {
        if (
            player.getActiveStatusEffects().containsKey(LeaderCommandRegistry.HIGH_LIGHT.getEffectType()) &&
            !LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player)) &&
            !LEADER_MAP.containsKey(PlayerUtil.getName(player))
        ) {
            player.removeStatusEffect(LeaderCommandRegistry.HIGH_LIGHT.getEffectType());
        }

        if (LeaderCommandRegistry.LEADER_MAP.containsValue(PlayerUtil.getPlayerUUID(player))) {
            player.addStatusEffect(
                LeaderCommandRegistry.HIGH_LIGHT
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
