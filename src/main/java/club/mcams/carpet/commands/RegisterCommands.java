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


import club.mcams.carpet.commands.rule.customBlockBlastResistance.CustomBlockBlastResistanceCommandRegistry;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

import club.mcams.carpet.commands.rule.amsUpdateSuppressionCrashFix.AmsUpdateSuppressionCrashFixCommandRegistry;
import club.mcams.carpet.commands.rule.anvilInteractionDisabled.AnvilInteractionDisabledCommandRegistry;
import club.mcams.carpet.commands.rule.customBlockHardness.CustomBlockHardnessCommandRegistry;
import club.mcams.carpet.commands.rule.playerChunkLoadController.PlayerChunkLoadControllerCommandRegistry;

public class RegisterCommands {
    public static void registerCommands(
        CommandDispatcher<ServerCommandSource> dispatcher
        //#if MC>=11900
        //$$ , final CommandRegistryAccess commandBuildContext
        //#endif
    ) {
        AmsUpdateSuppressionCrashFixCommandRegistry.register(dispatcher);
        PlayerChunkLoadControllerCommandRegistry.register(dispatcher);
        AnvilInteractionDisabledCommandRegistry.register(dispatcher);
        //#if MC<11900
        CustomBlockHardnessCommandRegistry.register(dispatcher);
        CustomBlockBlastResistanceCommandRegistry.register(dispatcher);
        //#else
        //$$ CustomBlockHardnessCommandRegistry.register(dispatcher, commandBuildContext);
        //$$ CustomBlockBlastResistanceCommandRegistry.register(dispatcher, commandBuildContext);
        //#endif
    }
}
