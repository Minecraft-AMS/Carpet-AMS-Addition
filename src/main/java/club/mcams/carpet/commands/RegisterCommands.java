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

package club.mcams.carpet.commands;

import club.mcams.carpet.commands.rule.commandAtSomeOnePlayer.AtCommandRegistry;
import club.mcams.carpet.commands.rule.commandCarpetExtensionModWikiHyperlink.CarpetExtensionModWikiHyperlinkCommandRegistry;
import club.mcams.carpet.commands.rule.commandCustomAntiFireItems.CustomAntiFireItemsCommandRegistry;
import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import club.mcams.carpet.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelRegistry;
import club.mcams.carpet.commands.rule.commandCustomMovableBlock.CustomMovableBlockCommandRegistry;
import club.mcams.carpet.commands.rule.commandGetHeldItemID.GetHeldItemIDCommandRegistry;
import club.mcams.carpet.commands.rule.commandGetPlayerSkull.GetPlayerSkullCommandRegistry;
import club.mcams.carpet.commands.rule.commandGetSaveSize.GetSaveSizeCommandRegistry;
import club.mcams.carpet.commands.rule.commandGetSystemInfo.GetSystemInfoCommandRegistry;
import club.mcams.carpet.commands.rule.commandHere.HereCommandRegistry;
import club.mcams.carpet.commands.rule.commandPacketInternetGroper.PingCommandRegistry;
import club.mcams.carpet.commands.rule.commandPlayerLeader.LeaderCommandRegistry;
import club.mcams.carpet.commands.rule.commandWhere.WhereCommandRegistry;
import club.mcams.carpet.commands.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceCommandRegistry;
import club.mcams.carpet.commands.rule.amsUpdateSuppressionCrashFix.AmsUpdateSuppressionCrashFixCommandRegistry;
import club.mcams.carpet.commands.rule.commandAnvilInteractionDisabled.AnvilInteractionDisabledCommandRegistry;
import club.mcams.carpet.commands.rule.commandPlayerChunkLoadController.PlayerChunkLoadControllerCommandRegistry;
import club.mcams.carpet.commands.rule.commandGoto.GotoCommandRegistry;

import net.minecraft.server.command.ServerCommandSource;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

import com.mojang.brigadier.CommandDispatcher;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RegisterCommands {
    private static final Queue<Runnable> AMS_CMD_QUEUE = new ConcurrentLinkedQueue<>();

    public static void registerCommands(
        CommandDispatcher<ServerCommandSource> dispatcher
        //#if MC>=11900
        //$$ , final CommandRegistryAccess commandBuildContext
        //#endif
    ) {
        buildAmsCommandList(
            dispatcher
            //#if MC>=11900
            //$$ , commandBuildContext
            //#endif
        );
        AMS_CMD_QUEUE.forEach(Runnable::run);
    }

    @SuppressWarnings("CodeBlock2Expr")
    private static void buildAmsCommandList(
        CommandDispatcher<ServerCommandSource> dispatcher
        //#if MC>=11900
        //$$ , final CommandRegistryAccess commandBuildContext
        //#endif
    ) {
        AMS_CMD_QUEUE.add(() -> AmsUpdateSuppressionCrashFixCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> PlayerChunkLoadControllerCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> AnvilInteractionDisabledCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> {
            CustomBlockBlastResistanceCommandRegistry.register(
                dispatcher
                //#if MC>=11900
                //$$ , commandBuildContext
                //#endif
            );
        });
        AMS_CMD_QUEUE.add(() -> HereCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> WhereCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> LeaderCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> PingCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GetSaveSizeCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GetSystemInfoCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GotoCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> CustomCommandPermissionLevelRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> GetPlayerSkullCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> {
            CustomMovableBlockCommandRegistry.register(
                dispatcher
                //#if MC>=11900
                //$$ , commandBuildContext
                //#endif
            );
        });
        AMS_CMD_QUEUE.add(() -> GetHeldItemIDCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> {
            CustomAntiFireItemsCommandRegistry.register(
                dispatcher
                //#if MC>=11900
                //$$ , commandBuildContext
                //#endif
            );
        });
        AMS_CMD_QUEUE.add(() -> CarpetExtensionModWikiHyperlinkCommandRegistry.register(dispatcher));
        AMS_CMD_QUEUE.add(() -> {
            CustomBlockHardnessCommandRegistry.register(
                dispatcher
                //#if MC>=11900
                //$$ , commandBuildContext
                //#endif
            );
        });
        AMS_CMD_QUEUE.add(() -> AtCommandRegistry.register(dispatcher));
    }
}
