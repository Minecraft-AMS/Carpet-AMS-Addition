package club.mcams.carpet.mixin.optimizedDragonRespawn;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockPattern.class)
public interface BlockPatternTranslateInvoker {
    @Invoker("translate")
    static BlockPos translate(BlockPos pos, Direction forwards, Direction up, int offsetLeft, int offsetDown, int offsetForwards) {
        throw new AssertionError();
    }
}
