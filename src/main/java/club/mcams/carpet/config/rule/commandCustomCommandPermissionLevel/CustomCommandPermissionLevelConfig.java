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

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.commands.rule.commandCustomCommandPermissionLevel.CustomCommandPermissionLevelRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CustomCommandPermissionLevelConfig {
    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void loadFromJson(String configFilePath) {
        Gson gson = new Gson();
        Path path = Paths.get(configFilePath);
        CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.clear();
        if (Files.exists(path)) {
            try {
                String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                Type type = new TypeToken<Map<String, Integer>>() {}.getType();
                Map<String, Integer> simplifiedMap = gson.fromJson(json, type);
                CustomCommandPermissionLevelRegistry.COMMAND_PERMISSION_MAP.putAll(simplifiedMap);
            } catch (IOException e) {
                AmsServer.LOGGER.warn("Failed to load config", e);
            }
        }
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void saveToJson(Map<String, Integer> customCommandPermissionLevelMap, String configFilePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Integer> simplifiedMap = new HashMap<>(customCommandPermissionLevelMap);
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
        return server.getSavePath(WorldSavePath.ROOT).resolve("carpetamsaddition/customCommandPermissionLevel" + ".json").toString();
    }
}
