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

package club.mcams.carpet.config.rule.amsUpdateSuppressionCrashFix;

import club.mcams.carpet.AmsServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static club.mcams.carpet.commands.rule.amsUpdateSuppressionCrashFix.AmsUpdateSuppressionCrashFixCommandRegistry.amsUpdateSuppressionCrashFixForceMode;

public class ForceModeCommandConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveConfigToJson(MinecraftServer server) {
        File configFile = new File(getPath(server));
        JsonObject json = new JsonObject();
        if (!configFile.getParentFile().exists()) {
            boolean dirsCreated = configFile.getParentFile().mkdirs();
            if (!dirsCreated) {
                AmsServer.LOGGER.error("Failed to create directories for config file: {}", configFile.getParentFile().getPath());
                return;
            }
        }
        json.addProperty("amsUpdateSuppressionCrashFixForceMode", amsUpdateSuppressionCrashFixForceMode);
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            AmsServer.LOGGER.warn("Failed to save config", e);
        }
    }

    public static void loadConfigFromJson(MinecraftServer server) {
        File configFile = new File(getPath(server));
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (json.has("amsUpdateSuppressionCrashFixForceMode")) {
                    amsUpdateSuppressionCrashFixForceMode = json.get("amsUpdateSuppressionCrashFixForceMode").getAsBoolean();
                }
            } catch (IOException e) {
                AmsServer.LOGGER.warn("Failed to load config", e);
            }
        }
    }

    public static String getPath(MinecraftServer server) {
        return server.getSavePath(WorldSavePath.ROOT).resolve("carpetamsaddition/amsUpdateSuppressionCrashFixForceMode.json").toString();
    }
}
