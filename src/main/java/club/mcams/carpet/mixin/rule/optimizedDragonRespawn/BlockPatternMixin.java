package club.mcams.carpet.mixin.rule.optimizedDragonRespawn;

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

@Mixin(BlockPattern.class)
public abstract class BlockPatternMixin implements BlockPatternPartialSearchAroundInvoker {
    @Final @Shadow private int width;
    @Final @Shadow private int height;
    @Final @Shadow private int depth;
    @Shadow
    protected abstract BlockPattern.Result testTransform(BlockPos blockPos, Direction direction, Direction direction2, LoadingCache<BlockPos, CachedBlockPosition> loadingCache);

    @Override
    public BlockPattern.Result partialSearchAround(WorldView world, BlockPos pos) {
        LoadingCache<BlockPos, CachedBlockPosition> loadingCache = BlockPattern.makeCache(world, false);
        int i = Math.max(Math.max(this.width, this.height), this.depth);
        for (BlockPos blockPos : BlockPos.iterate(pos, pos.add(i - 1, 0, i - 1))) {
            for (Direction direction : Direction.values()) {
                for (Direction direction2 : Direction.values()) {
                    BlockPattern.Result result;
                    if (direction2 == direction || direction2 == direction.getOpposite() || (result = this.testTransform(blockPos, direction, direction2, loadingCache)) == null) continue;
                    return result;
                }
            }
        }
        return null;
    }
}
