package club.mcams.carpet.fake;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public interface BlockPatternPartialSearchAroundInvoker {
    default BlockPattern.Result partialSearchAround(WorldView world, BlockPos pos) {
        return null;
    }
}
