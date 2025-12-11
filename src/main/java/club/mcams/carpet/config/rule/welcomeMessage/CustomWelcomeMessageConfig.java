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

package club.mcams.carpet.config.rule.welcomeMessage;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Messenger;

import com.google.gson.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomWelcomeMessageConfig {
    private static final Translator translator = new Translator("rule.welcomeMessage");
    private static final CustomWelcomeMessageConfig CONFIG = new CustomWelcomeMessageConfig();

    private CustomWelcomeMessageConfig() {}

    public static CustomWelcomeMessageConfig getConfig() {
        return CONFIG;
    }

    private static void handleMessage(PlayerEntity player, MinecraftServer server) {
        try {
            Path path = server.getSavePath(WorldSavePath.ROOT).resolve("carpetamsaddition/welcomeMessage.json");
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                JsonObject defaultConfig = new JsonObject();
                JsonArray defaultMsg = new JsonArray();
                defaultMsg.add("§3§o " + translator.tr("modify_content_in").getString());
                defaultMsg.add("§a" + translator.tr("save_path").getString() + "/carpetamsaddition/welcomeMessage.json");
                defaultConfig.add("welcomeMessage", defaultMsg);
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)) {
                    new Gson().toJson(defaultConfig, writer);
                }
            }

            JsonObject config;
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8)) {
                config = JsonParser.parseReader(reader).getAsJsonObject();
            }

            JsonElement msgElement = config.get("welcomeMessage");
            if (msgElement.isJsonArray()) {
                JsonArray messages = msgElement.getAsJsonArray();
                for (JsonElement element : messages) {
                    String line = element.getAsString();
                    player.sendMessage(Messenger.s(line), false);
                }
            } else {
                String legacyMsg = msgElement.getAsString();
                for (String line : legacyMsg.split("\n")) {
                    player.sendMessage(Messenger.s(line.trim()), false);
                }
            }
        } catch (Exception e) {
            AmsServer.LOGGER.error("An error occurred while processing the welcome message configuration", e);
        }
    }

    public void sendWelcomeMessage(PlayerEntity player, MinecraftServer server) {
        if (AmsServerSettings.welcomeMessage) {
            handleMessage(player, server);
        }
    }
}
