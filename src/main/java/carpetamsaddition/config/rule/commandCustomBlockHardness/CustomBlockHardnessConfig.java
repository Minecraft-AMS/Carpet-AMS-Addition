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

package carpetamsaddition.config.rule.commandCustomBlockHardness;

import carpetamsaddition.AmsServer;
import carpetamsaddition.config.template.AbstractMapJsonConfig;
import carpetamsaddition.utils.IdentifierUtil;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.RegexTools;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.core.registries.BuiltInRegistries;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CustomBlockHardnessConfig extends AbstractMapJsonConfig<String, Float> {
    private static final CustomBlockHardnessConfig INSTANCE = new CustomBlockHardnessConfig();
    
    public CustomBlockHardnessConfig() {
        super(getConfigPath(MinecraftServerUtil.getServer()));
    }

    public static CustomBlockHardnessConfig getInstance() {
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
        storageMap.forEach((blockId, hardness) -> {
            try {
                BuiltInRegistries.BLOCK.get(IdentifierUtil.ofId(blockId)).map(entry -> entry.value().defaultBlockState()).ifPresent(state -> targetMap.put(state, hardness));
            } catch (Exception e) {
                AmsServer.LOGGER.error("Invalid block ID: {}", blockId, e);
            }
        });
    }

    public void saveBlockStates(Map<BlockState, Float> sourceMap) {
        Map<String, Float> storageMap = new HashMap<>();
        sourceMap.forEach((state, hardness) -> {
            String blockId = RegexTools.getBlockRegisterName(state.getBlock().toString());
            storageMap.put(blockId, hardness);
        });
        super.saveToJson(storageMap);
    }

    private static Path getConfigPath(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT).resolve("carpetamsaddition/custom_block_hardness" + ".json");
    }
}