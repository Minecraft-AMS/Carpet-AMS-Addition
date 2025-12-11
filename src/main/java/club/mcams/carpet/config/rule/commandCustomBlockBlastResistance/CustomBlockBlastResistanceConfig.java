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

package club.mcams.carpet.config.rule.commandCustomBlockBlastResistance;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.config.template.AbstractMapJsonConfig;
import club.mcams.carpet.utils.IdentifierUtil;
import club.mcams.carpet.utils.MinecraftServerUtil;
import club.mcams.carpet.utils.RegexTools;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.core.registries.BuiltInRegistries;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CustomBlockBlastResistanceConfig extends AbstractMapJsonConfig<String, Float> {
    private static final CustomBlockBlastResistanceConfig INSTANCE = new CustomBlockBlastResistanceConfig();

    public CustomBlockBlastResistanceConfig() {
        super(getConfigPath(MinecraftServerUtil.getServer()));
    }

    public static CustomBlockBlastResistanceConfig getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<String> getKeyType() {
        return String.class;
    }

    @Override
    protected Class<Float> getValueType() {
        return Float.class;
    }

    public void loadBlockStates(Map<BlockState, Float> targetMap) {
        Map<String, Float> storageMap = new HashMap<>();
        super.loadFromJson(storageMap);
        targetMap.clear();
        storageMap.forEach((blockId, resistance) -> {
            try {
                BuiltInRegistries.BLOCK.get(IdentifierUtil.ofId(blockId)).map(entry -> entry.value().defaultBlockState()).ifPresent(state -> targetMap.put(state, resistance));
            } catch (Exception e) {
                AmsServer.LOGGER.error("Invalid block ID: {}", blockId, e);
            }
        });
    }

    public void saveBlockStates(Map<BlockState, Float> sourceMap) {
        Map<String, Float> storageMap = new HashMap<>();
        sourceMap.forEach((state, resistance) -> {
            String blockId = RegexTools.getBlockRegisterName(state.getBlock().toString());
            storageMap.put(blockId, resistance);
        });
        saveToJson(storageMap);
    }

    private static Path getConfigPath(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("carpetamsaddition/custom_block_blast_resistance.json");
    }
}
