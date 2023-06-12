package club.mcams.carpet.function;

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
