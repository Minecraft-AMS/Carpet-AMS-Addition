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
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class LeaderCommandRegistry {
    private static final Translator translator = new Translator("command.leader");
    private static final String MSG_HEAD = "<commandPlayerLeader> ";
    private static final int GLOWING_TIME = Integer.MAX_VALUE;
    private static final Set<Integer> suggestionIntervalOptions = ImmutableSet.of(20, 40, 80, 160, 320, 640, -1024);
    private static final Map<String, Integer> PLAYER_TICK_INTERVAL = new ConcurrentHashMap<>();
    private static final Map<String, Integer> PLAYER_TICK_COUNTER = new ConcurrentHashMap<>();
    public static final MobEffectInstance HIGH_LIGHT = new MobEffectInstance(MobEffects.GLOWING, GLOWING_TIME);
    public static final Map<String, String> LEADER_MAP = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("leader")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandPlayerLeader))
            .then(literal("add")
            .then(argument("player", EntityArgument.player())
            .executes(context -> add(context.getSource().getServer(), EntityArgument.getPlayer(context, "player")))))
            .then(literal("remove")
            .then(argument("player", EntityArgument.player())
            .executes(context -> remove(context.getSource().getServer(), EntityArgument.getPlayer(context, "player")))))
            .then(literal("removeAll")
            .executes(context -> removeAll(context.getSource().getServer(), context.getSource().getPlayerOrException())))
            .then(literal("list")
            .executes(context -> list(context.getSource().getPlayerOrException())))
            .then(literal("help")
            .executes(context -> help(context.getSource().getPlayerOrException())))
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
        if (!LEADER_MAP.containsKey(getPlayerName(targetPlayer))) {
            Messenger.sendServerMessage(server,
                Messenger.s(
                    String.format(MSG_HEAD + getPlayerName(targetPlayer) + " " + translator.tr("is_not_leader").getString())
                ).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC)
            );
            return 0;
        }
        String playerUUID = getPlayerUUID(targetPlayer);
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
            List<String> needRemovePlayer = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : PLAYER_TICK_INTERVAL.entrySet()) {
                String playerUUID = entry.getKey();
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
            for (Map.Entry<String, Integer> entry : PLAYER_TICK_INTERVAL.entrySet()) {
                String playerUUID = entry.getKey();
                int interval = entry.getValue();
                int tickCounter = PLAYER_TICK_COUNTER.getOrDefault(playerUUID, 0);
                tickCounter++;
                if (tickCounter >= interval && MinecraftServerUtil.serverIsRunning()) {
                    Player player = MinecraftServerUtil.getServer().getPlayerList().getPlayer(UUID.fromString(playerUUID));
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
            LEADER_MAP.containsValue(getPlayerUUID(player)) &&
            LEADER_MAP.containsKey(getPlayerName(player));
    }

    private static int add(MinecraftServer server, Player targetPlayer) {
        if (!LEADER_MAP.containsValue(getPlayerUUID(targetPlayer))) {
            targetPlayer.addEffect(HIGH_LIGHT);
            Messenger.sendServerMessage(
                server,
                Messenger.s(
                    String.format("%s %s %s", MSG_HEAD, getPlayerName(targetPlayer), translator.tr("add").getString())
                ).withStyle(ChatFormatting.GRAY)
            );
            LEADER_MAP.put(getPlayerName(targetPlayer), getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(
                server,
                Messenger.s(
                    MSG_HEAD + getPlayerName(targetPlayer) + " " + translator.tr("is_already_leader").getString()
                ).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC)
            );
        }
        return 1;
    }

    private static int remove(MinecraftServer server, Player targetPlayer) {
        if (LEADER_MAP.containsValue(getPlayerUUID(targetPlayer))) {
            targetPlayer.removeEffect(HIGH_LIGHT.getEffect());
            Messenger.sendServerMessage(
                server,
                Messenger.s(
                    String.format("%s %s %s", MSG_HEAD, getPlayerName(targetPlayer), translator.tr("remove").getString())
                ).withStyle(ChatFormatting.GRAY)
            );
            LEADER_MAP.remove(getPlayerName(targetPlayer), getPlayerUUID(targetPlayer));
            saveToJson();
        } else {
            Messenger.sendServerMessage(
                server,
                Messenger.s(
                    String.format(MSG_HEAD + getPlayerName(targetPlayer) + " " + translator.tr("is_not_leader").getString())
                ).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC)
            );
        }
        return 1;
    }

    private static int removeAll(MinecraftServer server, Player player) {
        Iterator<Map.Entry<String, String>> iterator = LEADER_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String playerUUID = entry.getValue();
            Player targetPlayer = server.getPlayerList().getPlayer(UUID.fromString(playerUUID));
            if (targetPlayer != null) {
                targetPlayer.removeEffect(HIGH_LIGHT.getEffect());
            }
            iterator.remove();
        }
        LEADER_MAP.clear();
        saveToJson();
        player.displayClientMessage(
            Messenger.s(MSG_HEAD + translator.tr("removeAll").getString()).withStyle(ChatFormatting.YELLOW), false
        );
        return 1;
    }

    private static int list(Player player) {
        final String LINE = "-----------------------------------------";
        player.displayClientMessage(
            Messenger.s(
                translator.tr("list").getString() + "\n" + LINE
            ).withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), false
        );
        for (Map.Entry<String, String> entry : LEADER_MAP.entrySet()) {
            String playerName = entry.getKey();
            String playerUUID = getPlayerUUID(player);
            player.displayClientMessage(Messenger.s(playerName + " - " + playerUUID).withStyle(ChatFormatting.DARK_AQUA), false);
        }
        return 1;
    }

    private static int help(Player player) {
        String addHelp = translator.tr("help.add").getString();
        String removeHelp = translator.tr("help.remove").getString();
        String removeAllHelp = translator.tr("help.removeAll").getString();
        String broadcastLeaderPos = translator.tr("help.broadcast_leader_pos").getString();
        String listHelp = translator.tr("help.list").getString();
        player.displayClientMessage(
            Messenger.s(addHelp + "\n" + removeHelp + "\n" + removeAllHelp + "\n" + broadcastLeaderPos +  "\n" + listHelp).
            setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), false
        );
        return 1;
    }

    public static void onPlayerLoggedIn(Player player) {
        if (
            player.getActiveEffectsMap().containsKey(LeaderCommandRegistry.HIGH_LIGHT.getEffect()) &&
            !LEADER_MAP.containsValue(getPlayerUUID(player)) &&
            !LEADER_MAP.containsKey(getPlayerName(player))
        ) {
            player.removeEffect(LeaderCommandRegistry.HIGH_LIGHT.getEffect());
        }
        if (LeaderCommandRegistry.LEADER_MAP.containsValue(player.getStringUUID())) {
            player.addEffect(LeaderCommandRegistry.HIGH_LIGHT, player);
        }
    }

    private static String getPlayerName(Player player) {
        return player.getGameProfile().name();
    }

    private static String getPlayerUUID(Player player) {
        return player.getStringUUID();
    }

    private static void saveToJson() {
        LeaderConfig.getInstance().saveToJson(LEADER_MAP);
    }
}
