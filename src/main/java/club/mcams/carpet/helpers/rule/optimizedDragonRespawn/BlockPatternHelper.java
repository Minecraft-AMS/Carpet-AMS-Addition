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

package club.mcams.carpet.helpers.rule.optimizedDragonRespawn;

import club.mcams.carpet.mixin.rule.optimizedDragonRespawn.BlockPatternTestTransformInvoker;

import com.google.common.cache.LoadingCache;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

public class BlockPatternHelper {
    public static BlockPattern.Result partialSearchAround(BlockPattern pattern, WorldView world, BlockPos pos) {
        LoadingCache<BlockPos, CachedBlockPosition> loadingCache = BlockPattern.makeCache(world, false);
        int i = Math.max(Math.max(pattern.getWidth(), pattern.getHeight()), pattern.getDepth());
        for (BlockPos blockPos : BlockPos.iterate(pos, pos.add(i - 1, 0, i - 1))) {
            for (Direction direction : Direction.values()) {
                for (Direction direction2 : Direction.values()) {
                    BlockPattern.Result result;
                    if (direction2 == direction || direction2 == direction.getOpposite() || (result = ((BlockPatternTestTransformInvoker)pattern).invokeTestTransform(blockPos, direction, direction2, loadingCache)) == null) continue;
                    return result;
                }
            }
        }
        return null;
    }
}
