/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.config.rule.commandCustomCommandPermissionLevel;

import club.mcams.carpet.config.template.AbstractMapJsonConfig;
import club.mcams.carpet.utils.MinecraftServerUtil;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.nio.file.Path;

public class CustomCommandPermissionLevelConfig extends AbstractMapJsonConfig<String, Integer> {
    private static final CustomCommandPermissionLevelConfig INSTANCE = new CustomCommandPermissionLevelConfig();

    public CustomCommandPermissionLevelConfig() {
        super(getConfigPath(MinecraftServerUtil.getServer()));
    }

    public static CustomCommandPermissionLevelConfig getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<String> getKeyType() {
        return String.class;
    }

    @Override
    protected Class<Integer> getValueType() {
        return Integer.class;
    }

    private static Path getConfigPath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("carpetamsaddition/custom_command_permission_level.json");
    }
}
