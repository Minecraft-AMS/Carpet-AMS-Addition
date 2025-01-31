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

package club.mcams.carpet.config;

import club.mcams.carpet.commands.rule.commandCustomAntiFireItems.CustomAntiFireItemsCommandRegistry;
import club.mcams.carpet.commands.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceCommandRegistry;
import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import club.mcams.carpet.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelRegistry;
import club.mcams.carpet.commands.rule.commandCustomMovableBlock.CustomMovableBlockCommandRegistry;
import club.mcams.carpet.commands.rule.commandPlayerLeader.LeaderCommandRegistry;

import club.mcams.carpet.config.rule.amsUpdateSuppressionCrashFix.ForceModeCommandConfig;
import club.mcams.carpet.config.rule.commandAntiFireItems.CustomAntiFireItemsConfig;
import club.mcams.carpet.config.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceConfig;
import club.mcams.carpet.config.rule.commandCustomBlockHardness.CustomBlockHardnessConfig;
import club.mcams.carpet.config.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelConfig;
import club.mcams.carpet.config.rule.commandCustomMovableBlock.CustomMovableBlockConfig;
import club.mcams.carpet.config.rule.commandLeader.LeaderConfig;

import net.minecraft.server.MinecraftServer;

public class LoadConfigFromJson {
    public static void load(MinecraftServer server) {
        clearMemory();
        ForceModeCommandConfig.loadConfigFromJson(server);
        CustomBlockBlastResistanceConfig.loadFromJson(CustomBlockBlastResistanceConfig.getPath(server));
        LeaderConfig.loadFromJson(LeaderConfig.getPath(server));
        CustomCommandPermissionLevelConfig.loadFromJson(CustomCommandPermissionLevelConfig.getPath(server));
        CustomMovableBlockConfig.loadFromJson(CustomMovableBlockConfig.getPath(server));
        CustomAntiFireItemsConfig.loadFromJson(CustomAntiFireItemsConfig.getPath(server));
        CustomBlockHardnessConfig.loadFromJson(CustomBlockHardnessConfig.getPath(server));
    }

    private static void clearMemory() {
        CustomBlockBlastResistanceCommandRegistry.CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        LeaderCommandRegistry.LEADER_LIST.clear();
        CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.clear();
        CustomMovableBlockCommandRegistry.CUSTOM_MOVABLE_BLOCKS.clear();
        CustomAntiFireItemsCommandRegistry.CUSTOM_ANTI_FIRE_ITEMS.clear();
        CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.clear();
    }
}
