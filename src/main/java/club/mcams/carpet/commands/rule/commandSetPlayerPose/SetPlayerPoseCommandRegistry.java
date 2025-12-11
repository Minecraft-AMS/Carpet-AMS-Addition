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
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.ChatFormatting;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SetPlayerPoseCommandRegistry {
    private static final Translator tr = new Translator("command.playerPose");
    private static final List<String> POSE = Arrays.asList("spin_attack", "swimming", "sleeping", "fall_flying", "standing", "crouching", "dying");
    public static final Map<UUID, String> DO_POSE_MAP = new ConcurrentHashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("playerPose")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandSetPlayerPose))
            .then(Commands.argument("player", EntityArgument.player())
            .then(Commands.literal("set")
            .then(Commands.argument("pose", StringArgumentType.greedyString())
            .suggests(ListSuggestionProvider.of(POSE))
            .executes(ctx -> set(
                EntityArgument.getPlayer(ctx, "player"), ctx.getSource().getServer(), StringArgumentType.getString(ctx, "pose")
            )))))
            .then(Commands.argument("player", EntityArgument.player())
            .then(Commands.literal("stop")
            .executes(ctx -> stop(
                EntityArgument.getPlayer(ctx, "player"), ctx.getSource().getServer()
            ))))
            .then(Commands.literal("help")
            .executes(ctx -> help(ctx.getSource())))
        );
    }

    private static int set(ServerPlayer targetPlayer, MinecraftServer server, String pose) {
        DO_POSE_MAP.put(targetPlayer.getUUID(), pose);
        targetPlayer.setShiftKeyDown(true);
        triggerPoseUpdate(targetPlayer);
        broadcastSyncDatapack(targetPlayer, server);
        return 1;
    }

    private static int stop(ServerPlayer targetPlayer, MinecraftServer server) {
        DO_POSE_MAP.remove(targetPlayer.getUUID());
        triggerPoseUpdate(targetPlayer);
        broadcastSyncDatapack(targetPlayer, server);
        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(source, Messenger.formatting(tr.tr("set_help").append(Messenger.endl()).append(tr.tr("stop_help")), ChatFormatting.GRAY));
        return 1;
    }

    private static void broadcastSyncDatapack(ServerPlayer targetPlayer, MinecraftServer server) {
        NetworkUtil.broadcastDataPack(server, UpdatePlayerPosePayload_S2C.create(DO_POSE_MAP, targetPlayer.getUUID()));
    }

    private static void triggerPoseUpdate(ServerPlayer targetPlayer) {
        targetPlayer.setShiftKeyDown(true);
        Runnable stopSneaking = () -> targetPlayer.setShiftKeyDown(false);
        Runnable stopSneakingOnServerThread = () -> NetworkUtil.executeOnServerThread(stopSneaking);
        CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS).execute(stopSneakingOnServerThread);
    }
}
