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

import java.util.*;

public final class LocationTagIndex {
    public static final class Entry {
        public int id;
        public String name;
        public String dimension;
        public int x, y, z;

        public Entry(int id, String name, String dimension, int x, int y, int z) {
            this.id = id;
            this.name = name;
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String positionKey() {
            return dimension + ":" + x + "," + y + "," + z;
        }

        public String chunkKey() {
            return LocationTagIndex.chunkKey(dimension, x >> 4, z >> 4);
        }
    }

    private final Map<String, Entry> positions = new HashMap<>();
    private final Map<Integer, Entry> ids = new HashMap<>();
    private final Map<String, Set<String>> chunks = new HashMap<>();
    private int nextId = 1;
    private boolean needsNumbering;
    private List<Entry> ordered = Collections.emptyList();

    private static String chunkKey(String dimension, int x, int z) {
        return dimension + ":" + x + "," + z;
    }

    public void restore(List<Entry> entries, int nextId) {
        clear();
        this.nextId = Math.max(1, nextId);

        for (Entry entry : entries) {
            if (
                entry == null ||
                entry.id <= 0 ||
                entry.id == Integer.MAX_VALUE ||
                entry.name == null ||
                entry.name.trim().isEmpty() ||
                entry.dimension == null ||
                entry.dimension.isEmpty() ||
                ids.containsKey(entry.id) ||
                positions.containsKey(entry.positionKey())
            ) {
                throw new IllegalArgumentException("Invalid or duplicate location tag");
            }

            put(entry);
            this.nextId = Math.max(this.nextId, entry.id + 1);
        }

        needsNumbering = true;
    }

    private void put(Entry entry) {
        positions.put(entry.positionKey(), entry);
        ids.put(entry.id, entry);
        chunks.computeIfAbsent(entry.chunkKey(), key -> new HashSet<>()).add(entry.positionKey());
    }

    public boolean replaceChunk(String dimension, int chunkX, int chunkZ, List<Entry> found) {
        String chunk = chunkKey(dimension, chunkX, chunkZ);
        Set<String> remaining = new HashSet<>(chunks.getOrDefault(chunk, Collections.emptySet()));
        boolean changed = false;

        for (Entry candidate : found) {
            if (!Objects.equals(candidate.chunkKey(), chunk)) {
                throw new IllegalArgumentException("Location tag belongs to another chunk");
            }

            Entry old = positions.get(candidate.positionKey());
            remaining.remove(candidate.positionKey());

            if (old != null && Objects.equals(old.name, candidate.name)) {
                continue;
            }

            if (old == null && nextId == Integer.MAX_VALUE) {
                throw new IllegalStateException("Location tag IDs exhausted");
            }

            put(new Entry(old == null ? nextId++ : old.id, candidate.name, candidate.dimension, candidate.x, candidate.y, candidate.z));
            changed = true;
        }

        for (String key : remaining) {
            Entry old = positions.remove(key);
            ids.remove(old.id);
            chunks.get(chunk).remove(key);
            changed = true;
        }

        if (chunks.containsKey(chunk) && chunks.get(chunk).isEmpty()) {
            chunks.remove(chunk);
        }

        needsNumbering |= changed;

        return changed;
    }

    private void numberEntries() {
        if (!needsNumbering) {
            return;
        }

        ordered = new ArrayList<>(positions.values());
        ordered.sort(Comparator.comparing((Entry entry) -> entry.name).thenComparing(entry -> entry.dimension).thenComparingInt(entry -> entry.x).thenComparingInt(entry -> entry.y).thenComparingInt(entry -> entry.z));
        ids.clear();
        nextId = 1;

        for (Entry entry : ordered) {
            entry.id = nextId++;
            ids.put(entry.id, entry);
        }

        needsNumbering = false;
    }

    public List<Entry> entries(String dimension) {
        numberEntries();
        List<Entry> result = new ArrayList<>();

        for (Entry entry : ordered) {
            if (dimension == null || Objects.equals(dimension, entry.dimension)) {
                result.add(entry);
            }
        }

        return result;
    }

    public Entry get(int id) {
        numberEntries();
        return ids.get(id);
    }

    public int nextId() {
        numberEntries();
        return nextId;
    }

    public void clear() {
        positions.clear();
        ids.clear();
        chunks.clear();
        ordered = Collections.emptyList();
        nextId = 1;
        needsNumbering = false;
    }
}
