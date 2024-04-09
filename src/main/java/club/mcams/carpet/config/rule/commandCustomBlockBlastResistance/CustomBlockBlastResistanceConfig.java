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
import club.mcams.carpet.utils.RegexTools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static club.mcams.carpet.commands.rule.commandCustomBlockBlastResistance.CustomBlockBlastResistanceCommandRegistry.CUSTOM_BLOCK_BLAST_RESISTANCE_MAP;

public class CustomBlockBlastResistanceConfig {
    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void loadFromJson(String configFilePath) {
        Gson gson = new Gson();
        Path path = Paths.get(configFilePath);
        CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.clear();
        if (Files.exists(path)) {
            try {
                String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                Type type = new TypeToken<Map<String, Float>>() {}.getType();
                Map<String, Float> simplifiedMap = gson.fromJson(json, type);
                for (Map.Entry<String, Float> entry : simplifiedMap.entrySet()) {
                    BlockState state = Registry.BLOCK.get(new Identifier(entry.getKey())).getDefaultState();
                    CUSTOM_BLOCK_BLAST_RESISTANCE_MAP.put(state, entry.getValue());
                }
            } catch (IOException e) {
                AmsServer.LOGGER.warn("Failed to load config", e);
            }
        }
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void saveToJson(Map<BlockState, Float> customBlockMap, String configFilePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Float> simplifiedMap = new HashMap<>();
        for (Map.Entry<BlockState, Float> entry : customBlockMap.entrySet()) {
            BlockState state = entry.getKey().getBlock().getDefaultState();
            String blockName = RegexTools.getBlockRegisterName(state.getBlock().toString());
            simplifiedMap.put(blockName, entry.getValue());
        }
        String json = gson.toJson(simplifiedMap);
        try {
            Path path = Paths.get(configFilePath);
            Files.createDirectories(path.getParent());
            Files.write(path, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            AmsServer.LOGGER.warn("Failed to save config", e);
        }
    }

    public static String getPath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("carpetamsaddition/custom_block_Blast_Resistance" + ".json").toString();
    }
}
