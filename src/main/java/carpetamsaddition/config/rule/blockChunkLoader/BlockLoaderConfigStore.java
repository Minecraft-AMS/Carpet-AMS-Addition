/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.config.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BlockLoaderConfigStore {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<LoaderType, BlockLoaderConfig> CONFIGS = new EnumMap<>(LoaderType.class);
    private static Path path;

    private BlockLoaderConfigStore() {}

    public static BlockLoaderConfig get(LoaderType type) {
        return CONFIGS.computeIfAbsent(type, key -> new BlockLoaderConfig());
    }

    public static BlockLoaderConfig copy(LoaderType type) {
        return GSON.fromJson(GSON.toJson(get(type)), BlockLoaderConfig.class);
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void loadFromJson(MinecraftServer server) {
        clear();
        path = server.getWorldPath(LevelResource.ROOT).resolve("carpetamsaddition/block_chunkloader.json");

        for (LoaderType type : LoaderType.values()) {
            CONFIGS.put(type, new BlockLoaderConfig());
        }

        if (!Files.exists(path)) {
            return;
        }

        try {
            Map<String, BlockLoaderConfig> json = GSON.fromJson(
                new String(Files.readAllBytes(path), StandardCharsets.UTF_8),
                new TypeToken<Map<String, BlockLoaderConfig>>() {}.getType()
            );
            Map<LoaderType, BlockLoaderConfig> loaded = new EnumMap<>(LoaderType.class);

            for (LoaderType type : LoaderType.values()) {
                BlockLoaderConfig config = json.get(type.id);
                if (config != null) {
                    loaded.put(type, config);
                }
            }

            CONFIGS.putAll(loaded);
        } catch (Exception e) {
            CarpetAMSAdditionServer.LOGGER.error("Failed to read block chunk loader configuration; loaders have been disabled and the original file will not be overwritten: {}", path, e);
        }
    }

    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void commit(LoaderType type, BlockLoaderConfig config) throws Exception {
        Map<String, BlockLoaderConfig> data = new LinkedHashMap<>();

        for (LoaderType key : LoaderType.values()) {
            data.put(key.id, key == type ? config : get(key));
        }

        Files.createDirectories(path.getParent());
        Path temporary = Files.createTempFile(path.getParent(), "block_chunkloader-", ".tmp");

        try {
            Files.write(temporary, GSON.toJson(data).getBytes(StandardCharsets.UTF_8));

            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
            }

            CONFIGS.put(type, config);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    public static void clear() {
        CONFIGS.clear();
        path = null;
    }
}
