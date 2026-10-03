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

package carpetamsaddition.config;

import carpetamsaddition.commands.rule.commandCustomAntiFireItems.CustomAntiFireItemsCommand;
import carpetamsaddition.commands.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceCommand;
import carpetamsaddition.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommand;
import carpetamsaddition.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelCommand;
import carpetamsaddition.commands.rule.commandCustomMovableBlock.CustomMovableBlockCommand;
import carpetamsaddition.commands.rule.commandPlayerLeader.LeaderCommand;

import carpetamsaddition.config.rule.amsUpdateSuppressionCrashFix.ForceModeCommandConfig;
import carpetamsaddition.config.rule.commandAntiFireItems.CustomAntiFireItemsConfig;
import carpetamsaddition.config.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceConfig;
import carpetamsaddition.config.rule.commandCustomBlockHardness.CustomBlockHardnessConfig;
import carpetamsaddition.config.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelConfig;
import carpetamsaddition.config.rule.commandCustomMovableBlock.CustomMovableBlockConfig;
import carpetamsaddition.config.rule.commandLeader.LeaderConfig;

import net.minecraft.server.MinecraftServer;

public class LoadConfigFromJson {
    public static void load(MinecraftServer server) {
        ForceModeCommandConfig.loadConfigFromJson(server);
        CustomBlockBlastResistanceConfig.getInstance().loadBlockStates(CustomBlockBlastResistanceCommand.CUSTOM_BLOCK_BLAST_RESISTANCE_MAP);
        LeaderConfig.getInstance().loadFromJson(LeaderCommand.LEADER_MAP);
        CustomCommandPermissionLevelConfig.getInstance().loadFromJson(CustomCommandPermissionLevelCommand.COMMAND_PERMISSION_MAP);
        CustomMovableBlockConfig.getInstance().loadFromJson(CustomMovableBlockCommand.CUSTOM_MOVABLE_BLOCKS);
        CustomAntiFireItemsConfig.getInstance().loadFromJson(CustomAntiFireItemsCommand.CUSTOM_ANTI_FIRE_ITEMS);
        CustomBlockHardnessConfig.getInstance().loadBlockStates(CustomBlockHardnessCommand.CUSTOM_BLOCK_HARDNESS_MAP);
    }
}
