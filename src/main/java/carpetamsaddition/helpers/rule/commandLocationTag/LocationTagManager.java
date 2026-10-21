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

package carpetamsaddition.helpers.rule.commandLocationTag;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.config.rule.commandLocationTag.LocationTagConfig;
import carpetamsaddition.config.rule.commandLocationTag.LocationTagConfigStore;
import carpetamsaddition.utils.compat.DimensionWrapper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.util.*;

public final class LocationTagManager {
    private static final Map<ServerLevel, Map<Long, LevelChunk>> LOADED = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<Long>> PENDING = new IdentityHashMap<>();
    private static final LocationTagIndex INDEX = new LocationTagIndex();
    private static MinecraftServer server;
    private static LocationTagConfigStore store;
    private static LocationTagConfig data = new LocationTagConfig();
    private static boolean available, enabled, dirty;
    private static int ticks;

    private LocationTagManager() {}

    public static String dimension(ServerLevel level) {
        return DimensionWrapper.of(level.dimension()).getIdentifierString();
    }

    public static boolean isEnabled() {
        return !Objects.equals(CarpetAMSAdditionSettings.commandLocationTag, "false");
    }

    public static boolean available() {
        return available;
    }

    public static String marker() {
        return data.marker;
    }

    public static String sign() {
        return data.sign;
    }

    public static List<LocationTagIndex.Entry> entries(String dimension) {
        return INDEX.entries(dimension);
    }

    public static LocationTagIndex.Entry get(int id) {
        return INDEX.get(id);
    }

    public static void loadFromJson(MinecraftServer currentServer) {
        server = currentServer;
        store = new LocationTagConfigStore(server.getWorldPath(LevelResource.ROOT));
        INDEX.restore(Collections.emptyList(), 1);
        data = new LocationTagConfig();
        available = true;
        enabled = false;
        dirty = false;
        ticks = 0;

        try {
            data = store.load();
            INDEX.restore(data.entries, data.nextId);
        } catch (Exception e) {
            available = false;
            INDEX.clear();
            CarpetAMSAdditionServer.LOGGER.error("Failed to read location tags; original file will not be overwritten: {}", store.getPath(), e);
        }
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    public static void chunkLoaded(LevelChunk chunk, boolean loaded) {
        if (!(chunk.getLevel() instanceof ServerLevel)) {
            return;
        }

        ServerLevel level = (ServerLevel) chunk.getLevel();
        long key = chunkKey(chunkX(chunk), chunkZ(chunk));

        if (loaded) {
            LOADED.computeIfAbsent(level, ignored -> new HashMap<>()).put(key, chunk);
            changed(chunk);
        } else {
            Map<Long, LevelChunk> chunks = LOADED.get(level);

            if (chunks != null) {
                chunks.remove(key);
                if (chunks.isEmpty()) {
                    LOADED.remove(level);
                }
            }

            Set<Long> pending = PENDING.get(level);

            if (pending != null) {
                pending.remove(key);
            }
        }
    }

    private static long chunkKey(int x, int z) {
        return ((long) z << 32) | Integer.toUnsignedLong(x);
    }

    private static int chunkX(LevelChunk chunk) {
        //#if MC>=260000
        //$$ return chunk.getPos().x();
        //#else
        return chunk.getPos().x;
        //#endif
    }

    private static int chunkZ(LevelChunk chunk) {
        //#if MC>=260000
        //$$ return chunk.getPos().z();
        //#else
        return chunk.getPos().z;
        //#endif
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    public static void changed(LevelChunk chunk) {
        if (available && isEnabled() && chunk.getLevel() instanceof ServerLevel) {
            ServerLevel level = (ServerLevel) chunk.getLevel();
            PENDING.computeIfAbsent(level, ignored -> new LinkedHashSet<>()).add(chunkKey(chunkX(chunk), chunkZ(chunk)));
        }
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    public static void changed(BlockEntity entity) {
        if (!(entity instanceof SignBlockEntity) || !(entity.getLevel() instanceof ServerLevel)) {
            return;
        }

        ServerLevel level = (ServerLevel) entity.getLevel();
        Map<Long, LevelChunk> chunks = LOADED.get(level);

        if (chunks != null) {
            LevelChunk chunk = chunks.get(chunkKey(entity.getBlockPos().getX() >> 4, entity.getBlockPos().getZ() >> 4));
            if (chunk != null) {
                changed(chunk);
            }
        }
    }

    public static int rescan() {
        int count = 0;

        for (Map.Entry<ServerLevel, Map<Long, LevelChunk>> world : LOADED.entrySet()) {
            if (world.getKey().getServer() == server) {
                for (LevelChunk chunk : world.getValue().values()) { changed(chunk); count++; }
            }
        }

        return count;
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    private static void scan(ServerLevel level, LevelChunk chunk) {
        List<LocationTagIndex.Entry> found = new ArrayList<>();
        String dimension = dimension(level);

        for (BlockEntity entity : chunk.getBlockEntities().values()) {
            if (entity instanceof SignBlockEntity && !entity.isRemoved() && LocationTagSignCompat.type(entity.getBlockState().getBlock()).equals(data.sign)) {
                SignBlockEntity sign = (SignBlockEntity) entity;
                String name = LocationTagSignCompat.line(sign, 1).trim() + LocationTagSignCompat.line(sign, 2).trim() + LocationTagSignCompat.line(sign, 3).trim();
                if (LocationTagSignCompat.line(sign, 0).trim().equals(data.marker) && !name.isEmpty()) {
                    found.add(new LocationTagIndex.Entry(0, name, dimension, entity.getBlockPos().getX(), entity.getBlockPos().getY(), entity.getBlockPos().getZ()));
                }
            }
        }

        dirty |= INDEX.replaceChunk(dimension, chunkX(chunk), chunkZ(chunk), found);
    }

    public static void tick(MinecraftServer currentServer) {
        if (server != currentServer || !available) {
            return;
        }

        boolean active = isEnabled();

        if (active && !enabled) {
            rescan();
        }

        enabled = active;

        if (!active) {
            PENDING.clear();
        }

        if (active) {
            drain(32);
        }

        if (++ticks % 100 == 0 && dirty) {
            saveQuietly();
        }
    }

    private static void drain(int budget) {
        Iterator<Map.Entry<ServerLevel, Set<Long>>> worlds = PENDING.entrySet().iterator();

        while (worlds.hasNext() && budget > 0) {
            Map.Entry<ServerLevel, Set<Long>> world = worlds.next();
            Iterator<Long> keys = world.getValue().iterator();

            while (keys.hasNext() && budget > 0) {
                Long key = keys.next(); keys.remove(); budget--;
                LevelChunk chunk = LOADED.getOrDefault(world.getKey(), Collections.emptyMap()).get(key);
                if (chunk != null && world.getKey().getServer() == server) {
                    scan(world.getKey(), chunk);
                }
            }

            if (world.getValue().isEmpty()) {
                worlds.remove();
            }
        }
    }

    public static void configure(String marker, String sign) throws IOException {
        if (!available) {
            throw new IOException("Location tag storage unavailable");
        }

        if (!LocationTagConfig.validMarker(marker)) {
            throw new IllegalArgumentException("Invalid location marker");
        }

        if (!LocationTagSignCompat.types().contains(sign)) {
            throw new IllegalArgumentException("Unsupported sign type");
        }

        LocationTagConfig old = data;
        data = new LocationTagConfig();
        data.marker = marker.trim();
        data.sign = sign;
        data.nextId = INDEX.nextId();

        try {
            store.save(data);
        } catch (IOException e) {
            data = old;
            throw e;
        }

        INDEX.clear();
        dirty = false;
        rescan();
    }

    private static void saveQuietly() {
        try {
            data.entries = INDEX.entries(null);
            data.nextId = INDEX.nextId();
            store.save(data);
            dirty = false;
        } catch (IOException e) {
            CarpetAMSAdditionServer.LOGGER.error("Failed to save location tags: {}", store.getPath(), e);
        }
    }

    public static void close(MinecraftServer currentServer) {
        if (server == currentServer && available) {
            if (isEnabled()) {
                drain(Integer.MAX_VALUE);
            }

            if (dirty) {
                saveQuietly();
            }
        }

        LOADED.clear();
        PENDING.clear();
        INDEX.clear();
        server = null; store = null; available = false;
    }
}
