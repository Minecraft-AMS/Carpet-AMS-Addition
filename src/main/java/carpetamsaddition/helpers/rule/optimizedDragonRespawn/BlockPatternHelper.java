/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
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

package carpetamsaddition.helpers.rule.optimizedDragonRespawn;

import carpetamsaddition.mixin.rule.optimizedDragonRespawn.BlockPatternMixin;

import com.google.common.cache.LoadingCache;

import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.NotNull;

public class BlockPatternHelper {
    public static BlockPattern.BlockPatternMatch partialSearchAround(BlockPattern pattern, LevelReader world, BlockPos pos) {
        LoadingCache<@NotNull BlockPos, @NotNull BlockInWorld> loadingCache = BlockPattern.createLevelCache(world, false);
        int i = Math.max(Math.max(pattern.getWidth(), pattern.getHeight()), pattern.getDepth());

        for (BlockPos blockPos : BlockPos.betweenClosed(pos, pos.offset(i - 1, 0, i - 1))) {
            for (Direction direction : Direction.values()) {
                for (Direction direction2 : Direction.values()) {
                    BlockPattern.BlockPatternMatch result;
                    if (direction2 == direction || direction2 == direction.getOpposite() || (result = ((BlockPatternMixin) pattern).invokeMatches(blockPos, direction, direction2, loadingCache)) == null) continue;
                    return result;
                }
            }
        }

        return null;
    }
}
