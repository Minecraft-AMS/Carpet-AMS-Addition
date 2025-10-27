/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.commands.rule.commandSetPlayerPose;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.commands.suggestionProviders.ListSuggestionProvider;
import club.mcams.carpet.network.payloads.rule.commandSetPlayerPose.UpdatePlayerPosePayload_S2C;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SetPlayerPoseCommandRegistry {
    private static final Translator tr = new Translator("command.playerPose");
    private static final List<String> POSE = Arrays.asList("spin_attack", "swimming", "sleeping", "fall_flying", "standing", "crouching", "dying");
    public static final Map<UUID, String> DO_POSE_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("playerPose")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandSetPlayerPose))
            .then(CommandManager.argument("player", EntityArgumentType.player())
            .then(CommandManager.literal("set")
            .then(CommandManager.argument("pose", StringArgumentType.greedyString())
            .suggests(ListSuggestionProvider.of(POSE))
            .executes(ctx -> set(
                EntityArgumentType.getPlayer(ctx, "player"), ctx.getSource().getServer(), StringArgumentType.getString(ctx, "pose")
            )))))
            .then(CommandManager.argument("player", EntityArgumentType.player())
            .then(CommandManager.literal("stop")
            .executes(ctx -> stop(
                EntityArgumentType.getPlayer(ctx, "player"), ctx.getSource().getServer()
            ))))
            .then(CommandManager.literal("help")
            .executes(ctx -> help(ctx.getSource())))
        );
    }

    private static int set(ServerPlayerEntity targetPlayer, MinecraftServer server, String pose) {
        DO_POSE_MAP.put(targetPlayer.getUuid(), pose);
        targetPlayer.setSneaking(true);
        triggerPoseUpdate(targetPlayer);
        broadcastSyncDatapack(targetPlayer, server);
        return 1;
    }

    private static int stop(ServerPlayerEntity targetPlayer, MinecraftServer server) {
        DO_POSE_MAP.remove(targetPlayer.getUuid());
        triggerPoseUpdate(targetPlayer);
        broadcastSyncDatapack(targetPlayer, server);
        return 1;
    }

    private static int help(ServerCommandSource source) {
        Messenger.tell(source, Messenger.formatting((BaseText) tr.tr("set_help").append(Messenger.endl()).append(tr.tr("stop_help")), Formatting.GRAY));
        return 1;
    }

    private static void broadcastSyncDatapack(ServerPlayerEntity targetPlayer, MinecraftServer server) {
        NetworkUtil.broadcastDataPack(server, UpdatePlayerPosePayload_S2C.create(DO_POSE_MAP, targetPlayer.getUuid()));
    }

    private static void triggerPoseUpdate(ServerPlayerEntity targetPlayer) {
        targetPlayer.setSneaking(true);
        Runnable stopSneaking = () -> targetPlayer.setSneaking(false);
        Runnable stopSneakingOnServerThread = () -> NetworkUtil.executeOnServerThread(stopSneaking);
        CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS).execute(stopSneakingOnServerThread);
    }
}
