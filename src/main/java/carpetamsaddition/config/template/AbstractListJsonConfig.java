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

package carpetamsaddition.config.template;

import carpetamsaddition.CarpetAMSAdditionServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class AbstractListJsonConfig<T> {
    protected final Path configPath;
    protected final Gson gson;
    private final Type listType;

    protected AbstractListJsonConfig(Path configPath) {
        this.configPath = configPath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.listType = TypeToken.getParameterized(List.class, getElementType()).getType();
    }

    protected abstract Class<T> getElementType();

    public void loadFromJson(List<T> targetList) {
        targetList.clear();

        if (Files.notExists(configPath)) {
            return;
        }

        try {
            byte[] bytes = Files.readAllBytes(configPath);
            List<T> loadedList = gson.fromJson(new String(bytes, StandardCharsets.UTF_8), listType);
            if (loadedList != null) {
                targetList.addAll(loadedList);
            }
        } catch (IOException e) {
            CarpetAMSAdditionServer.LOGGER.error("Failed to load config: {}", configPath, e);
        }
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public void saveToJson(List<T> list) {
        try {
            Files.createDirectories(configPath.getParent());
            Files.write(configPath, gson.toJson(list, listType).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            CarpetAMSAdditionServer.LOGGER.error("Failed to save config: {}", configPath, e);
        }
    }
}
