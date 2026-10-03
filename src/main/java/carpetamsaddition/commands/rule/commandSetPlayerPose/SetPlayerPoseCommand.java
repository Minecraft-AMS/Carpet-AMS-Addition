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

package carpetamsaddition.commands.rule.commandSetPlayerPose;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.api.command.CommandPath;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messenger.Messenger;
import carpetamsaddition.utils.NetworkUtil;
import carpetamsaddition.api.command.suggestionProviders.ListSuggestionProvider;
import carpetamsaddition.network.payloads.rule.commandSetPlayerPose.UpdatePlayerPosePayload_S2C;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SetPlayerPoseCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.playerPose");
    private static final List<String> POSE = Arrays.asList("spin_attack", "swimming", "sleeping", "fall_flying", "standing", "crouching", "dying");
    public static final Map<UUID, String> DO_POSE_MAP = new ConcurrentHashMap<>();
    @Override
    public void define(CommandBuilder command) {
        command.name("playerPose")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandSetPlayerPose)
            .route(CommandPath.path().argument(Arguments.player("player")).literal("set").argument(Arguments.suggests(Arguments.greedyString("pose"), ListSuggestionProvider.of(POSE))))
            .executes(c -> set(c.get(Arguments.player("player")), c.get(Arguments.greedyString("pose"))))
            .route(CommandPath.path().argument(Arguments.player("player")).literal("stop"))
            .executes(c -> stop(c.get(Arguments.player("player"))))
            .route("help").executes(c -> help(c.source()));
    }

    private static int set(ServerPlayer targetPlayer, String pose) {
        DO_POSE_MAP.put(targetPlayer.getUUID(), pose);
        targetPlayer.setShiftKeyDown(true);
        broadcastSyncDatapack(targetPlayer);
        triggerPoseUpdate(targetPlayer);
        return 1;
    }

    private static int stop(ServerPlayer targetPlayer) {
        DO_POSE_MAP.remove(targetPlayer.getUUID());
        broadcastSyncDatapack(targetPlayer);
        triggerPoseUpdate(targetPlayer);
        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(
            source, Messenger.f(
                Messenger.c(
                    tr.tr("set_help"),
                    Messenger.endl(),
                    tr.tr("stop_help"),
                    Messenger.endl()
                ), Layout.GRAY
            )
        );

        return 1;
    }

    private static void broadcastSyncDatapack(ServerPlayer targetPlayer) {
        NetworkUtil.broadcastDataPack(UpdatePlayerPosePayload_S2C.create(DO_POSE_MAP, targetPlayer.getUUID()), NetworkUtil.SendMode.NEED_SUPPORT);
    }

    private static void triggerPoseUpdate(ServerPlayer targetPlayer) {
        targetPlayer.setShiftKeyDown(true);
        Runnable stopSneaking = () -> targetPlayer.setShiftKeyDown(false);
        Runnable stopSneakingOnServerThread = () -> NetworkUtil.executeOnServerThread(stopSneaking);
        CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS).execute(stopSneakingOnServerThread);
    }
}
