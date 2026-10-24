/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.config.rule.blockChunkLoader;

import carpetamsaddition.helpers.rule.blockChunkLoader.LoaderCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public final class BlockLoaderConfig {
    public static final int MAX_TIME = 300;
    public static final List<String> RANGES = Arrays.asList("1x1", "3x3", "5x5", "7x7", "9x9");
    public boolean enabled;
    public int time = MAX_TIME;
    public String range = "3x3";
    public int nextId = 1;
    public Map<String, Group> groups = new LinkedHashMap<>();

    public BlockLoaderConfig() {
        groups.put("default", new Group());
    }

    public static final class Group {
        public boolean enabled = true;
        public boolean any;
        public List<LoaderCondition> conditions = new ArrayList<>();

        public LoaderCondition.Result evaluate(ServerLevel world, BlockPos origin) {
            boolean unknown = false;

            for (LoaderCondition condition : conditions) {
                LoaderCondition.Result result = condition.evaluate(world, origin);

                if (any && result == LoaderCondition.Result.MATCH) {
                    return result;
                }

                if (!any && result == LoaderCondition.Result.FAIL) {
                    return result;
                }

                unknown |= result == LoaderCondition.Result.UNKNOWN;
            }

            return unknown ? LoaderCondition.Result.UNKNOWN : any ? LoaderCondition.Result.FAIL : LoaderCondition.Result.MATCH;
        }
    }

    public void renumberConditions() {
        nextId = 1;
        for (Group group : groups.values()) {
            for (LoaderCondition condition : group.conditions) {
                condition.id = nextId++;
            }
        }
    }

    public boolean matches(ServerLevel world, BlockPos origin) {
        for (Group group : groups.values()) {
            if (!group.enabled || group.conditions.isEmpty()) {
                continue;
            }

            if (group.evaluate(world, origin) != LoaderCondition.Result.MATCH) {
                return false;
            }
        }
        return true;
    }

    public int ticketRadius() {
        return (Integer.parseInt(range.substring(0, range.indexOf('x'))) - 1) / 2 + 2;
    }
}
