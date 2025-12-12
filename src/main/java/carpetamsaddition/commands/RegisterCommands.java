/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
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

package carpetamsaddition.commands;

import carpetamsaddition.commands.debug.network.AmspCommandRegistry;
import carpetamsaddition.commands.rule.commandAtSomeOnePlayer.AtCommandRegistry;
import carpetamsaddition.commands.rule.commandCarpetExtensionModWikiHyperlink.CarpetExtensionModWikiHyperlinkCommandRegistry;
import carpetamsaddition.commands.rule.commandCustomAntiFireItems.CustomAntiFireItemsCommandRegistry;
import carpetamsaddition.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import carpetamsaddition.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelRegistry;
import carpetamsaddition.commands.rule.commandCustomMovableBlock.CustomMovableBlockCommandRegistry;
import carpetamsaddition.commands.rule.commandGetClientPlayerFps.GetClientPlayerFpsRegistry;
import carpetamsaddition.commands.rule.commandGetHeldItemID.GetHeldItemIDCommandRegistry;
import carpetamsaddition.commands.rule.commandGetPlayerSkull.GetPlayerSkullCommandRegistry;
import carpetamsaddition.commands.rule.commandGetSaveSize.GetSaveSizeCommandRegistry;
import carpetamsaddition.commands.rule.commandGetSystemInfo.GetSystemInfoCommandRegistry;
import carpetamsaddition.commands.rule.commandHere.HereCommandRegistry;
import carpetamsaddition.commands.rule.commandPacketInternetGroper.PingCommandRegistry;
import carpetamsaddition.commands.rule.commandPlayerLeader.LeaderCommandRegistry;
import carpetamsaddition.commands.rule.commandSetPlayerPose.SetPlayerPoseCommandRegistry;
import carpetamsaddition.commands.rule.commandWhere.WhereCommandRegistry;
import carpetamsaddition.commands.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceCommandRegistry;
import carpetamsaddition.commands.rule.amsUpdateSuppressionCrashFix.AmsUpdateSuppressionCrashFixCommandRegistry;
import carpetamsaddition.commands.rule.commandAnvilInteractionDisabled.AnvilInteractionDisabledCommandRegistry;
import carpetamsaddition.commands.rule.commandPlayerChunkLoadController.PlayerChunkLoadControllerCommandRegistry;
import carpetamsaddition.commands.rule.commandGoto.GotoCommandRegistry;

import net.minecraft.commands.CommandSourceStack;
//#if MC>=11900
import net.minecraft.commands.CommandBuildContext;
//#endif

import com.mojang.brigadier.CommandDispatcher;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RegisterCommands {
    private static final Queue<Runnable> AMS_CMD_QUEUE = new ConcurrentLinkedQueue<>();

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, final CommandBuildContext commandBuildContext) {
        buildAmsCommandList(dispatcher, commandBuildContext);
        AMS_CMD_QUEUE.forEach(Runnable::run);
    }

    private static void buildAmsCommandList(CommandDispatcher<CommandSourceStack> dispatcher, final CommandBuildContext commandBuildContext) {
        AMS_CMD_QUEUE.add(() -> AmsUpdateSuppressionCrashFixCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> PlayerChunkLoadControllerCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> AnvilInteractionDisabledCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> CustomBlockBlastResistanceCommandRegistry.register(dispatcher, commandBuildContext));
        AMS_CMD_QUEUE.add(() -> HereCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> WhereCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> LeaderCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> PingCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GetSaveSizeCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GetSystemInfoCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GotoCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> CustomCommandPermissionLevelRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GetPlayerSkullCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> CustomMovableBlockCommandRegistry.register(dispatcher, commandBuildContext));
        AMS_CMD_QUEUE.add(() -> GetHeldItemIDCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> CustomAntiFireItemsCommandRegistry.register(dispatcher, commandBuildContext));
        AMS_CMD_QUEUE.add(() -> CarpetExtensionModWikiHyperlinkCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> CustomBlockHardnessCommandRegistry.register(dispatcher, commandBuildContext));
        AMS_CMD_QUEUE.add(() -> AtCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GetClientPlayerFpsRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> SetPlayerPoseCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> AmspCommandRegistry.register(dispatcher));
    }
}
