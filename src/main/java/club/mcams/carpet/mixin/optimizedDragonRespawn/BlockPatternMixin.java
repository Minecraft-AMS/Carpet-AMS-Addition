package club.mcams.carpet.mixin.optimizedDragonRespawn;

import club.mcams.carpet.fake.BlockPatternPartialSearchAroundInvoker;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(BlockPattern.class)
public abstract class BlockPatternMixin implements BlockPatternPartialSearchAroundInvoker {
    @Final @Shadow private int width;
    @Final @Shadow private int height;
    @Final @Shadow private int depth;
    @Final @Shadow private Predicate<CachedBlockPosition>[][][] pattern;
    /*
    @Shadow
    protected abstract BlockPattern.Result testTransform(BlockPos blockPos, Direction direction, Direction direction2, LoadingCache<BlockPos, CachedBlockPosition> loadingCache);
     */

    @Override
    public BlockPattern.Result partialSearchAround(WorldView world, BlockPos pos) {
        LoadingCache<BlockPos, CachedBlockPosition> loadingCache = BlockPattern.makeCache(world, false);
        int i = Math.max(Math.max(this.width, this.height), this.depth);
        for (BlockPos blockPos : BlockPos.iterate(pos, pos.add(i - 1, 0, i - 1))) {
            for (Direction direction : Direction.values()) {
                for (Direction direction2 : Direction.values()) {
                    BlockPattern.Result result;
                    if (direction2 == direction || direction2 == direction.getOpposite() || (result = this.testEndPortalTransform(blockPos, direction, direction2, loadingCache)) == null) continue;
                    return result;
                }
            }
        }
        return null;
    }

    /*
    "       ", "       ", "       ", "   #   ", "       ", "       ", "       "
    "       ", "       ", "       ", "   #   ", "       ", "       ", "       "
    "       ", "       ", "       ", "   #   ", "       ", "       ", "       "
    "  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  "
    "       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       "
     */
    private boolean testEndPortalTransformBlock(BlockPos frontTopLeft, Direction forwards, Direction up, LoadingCache<BlockPos, CachedBlockPosition> cache, int i,int j,int k) {
        return this.pattern[k][j][i].test((CachedBlockPosition)cache.getUnchecked(BlockPatternTranslateInvoker.translate(frontTopLeft, forwards, up, i, j, k)));
    }
    private final static int[][] cachePattern = new int[][]{
            {0,1,2}, {0,1,3}, {0,1,4},
            {0,2,1}, {0,2,2}, {0,2,3}, {0,2,4},{0,2,5},
            {0,3,1}, {0,3,2}, {0,3,3}, {0,3,4},{0,3,5},
            {0,4,1}, {0,4,2}, {0,4,3}, {0,4,4},{0,4,5},
            {0,5,2}, {0,5,3}, {0,5,4},

            {1,0,2}, {1,0,3}, {1,0,4},
            {1,1,1}, {1,1,5},
            {1,2,0}, {1,2,6},
            {1,3,0}, {1,3,3}, {1,3,6},
            {1,4,0}, {1,4,6},
            {1,5,1}, {1,5,5},
            {1,6,2}, {1,6,3}, {1,6,4},

            {2,3,3},
            {3,3,3},
            {4,3,3}
    };
    private BlockPattern.Result testEndPortalTransform(BlockPos frontTopLeft, Direction forwards, Direction up, LoadingCache<BlockPos, CachedBlockPosition> cache) {

        for (int[] pos: cachePattern) {
            int i=pos[1],j=pos[2],k=pos[0];
            if (!this.testEndPortalTransformBlock(frontTopLeft, forwards, up,  cache, i, j, k)) {
                return null;
            }
        }
        return new BlockPattern.Result(frontTopLeft, forwards, up, cache, this.width, this.height, this.depth);
    }
}
