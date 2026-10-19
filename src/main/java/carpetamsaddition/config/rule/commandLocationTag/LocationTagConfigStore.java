/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.config.rule.commandLocationTag;

import carpetamsaddition.helpers.rule.commandLocationTag.LocationTagIndex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class LocationTagConfigStore {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;
    private boolean available;

    public LocationTagConfigStore(Path worldRoot) {
        this.path = worldRoot.resolve("carpetamsaddition/location_tags.json");
    }

    public Path getPath() {
        return path;
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public LocationTagConfig load() throws IOException {
        available = false;
        try {
            LocationTagConfig config = Files.exists(path) ? GSON.fromJson(new String(Files.readAllBytes(path), StandardCharsets.UTF_8), LocationTagConfig.class) : new LocationTagConfig();

            if (config == null) {
                throw new IOException("Invalid location tag file");
            }

            config.validate();
            new LocationTagIndex().restore(config.entries, config.nextId);
            available = true;

            return config;
        } catch (RuntimeException e) {
            throw new IOException("Invalid location tag file", e);
        }
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public void save(LocationTagConfig snapshot) throws IOException {
        if (!available) {
            throw new IOException("Location tag storage unavailable");
        }

        snapshot.validate();
        Files.createDirectories(path.getParent());
        Path temporary = Files.createTempFile(path.getParent(), "location_tags-", ".tmp");

        try {
            Files.write(temporary, GSON.toJson(snapshot).getBytes(StandardCharsets.UTF_8));
            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
