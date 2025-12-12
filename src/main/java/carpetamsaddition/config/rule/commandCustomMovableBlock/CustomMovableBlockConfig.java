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

package carpetamsaddition.config.rule.commandCustomMovableBlock;

import carpetamsaddition.config.template.AbstractListJsonConfig;
import carpetamsaddition.utils.MinecraftServerUtil;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;

public class CustomMovableBlockConfig extends AbstractListJsonConfig<String> {
    private static final CustomMovableBlockConfig INSTANCE = new CustomMovableBlockConfig();

    public CustomMovableBlockConfig() {
        super(getPath(MinecraftServerUtil.getServer()));
    }

    public static CustomMovableBlockConfig getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<String> getElementType() {
        return String.class;
    }

    private static Path getPath(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("carpetamsaddition/custom_movable_block" + ".json");
    }
}
