package club.mcams.carpet.mixin.rule.optimizedDragonRespawn;

import com.google.common.cache.LoadingCache;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockPattern.class)
public interface BlockPatternTestTransformInvoker {
    @Invoker("testTransform")
    BlockPattern.Result invokeTestTransform(BlockPos frontTopLeft, Direction forwards, Direction up, LoadingCache<BlockPos, CachedBlockPosition> cache);
}
