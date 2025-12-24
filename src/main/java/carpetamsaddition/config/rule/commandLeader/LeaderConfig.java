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

package carpetamsaddition.config.rule.commandLeader;

import carpetamsaddition.config.template.AbstractMapJsonConfig;
import carpetamsaddition.utils.MinecraftServerUtil;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.UUID;

public class LeaderConfig extends AbstractMapJsonConfig<String, UUID> {
    private static final LeaderConfig INSTANCE = new LeaderConfig();

    public LeaderConfig() {
        super(getConfigPath(MinecraftServerUtil.getServer()));
    }

    public static LeaderConfig getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<String> getKeyType() {
        return String.class;
    }

    @Override
    protected Class<UUID> getValueType() {
        return UUID.class;
    }

    private static Path getConfigPath(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("carpetamsaddition/leader.json");
    }
}
