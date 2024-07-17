package club.mcams.carpet.mixin.rule.softBlock;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockMixin implements AbstractBlockStateInvoker{
    @Inject(method = "getHardness", at = @At("HEAD"), cancellable = true)
    private void modifyDeepslateHardness(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        //#if MC>=11700
        if (AmsServerSettings.softDeepslate && this.invokeGetBlock().equals(Blocks.DEEPSLATE)) {
            cir.setReturnValue(Blocks.STONE.getDefaultState().getHardness(world, pos));
            cir.cancel();
        }
        //#endif

        if (AmsServerSettings.softObsidian && (this.invokeGetBlock().equals(Blocks.OBSIDIAN) || this.invokeGetBlock().equals(Blocks.CRYING_OBSIDIAN))) {
            cir.setReturnValue(Blocks.END_STONE.getDefaultState().getHardness(world, pos));
            cir.cancel();
        }
    }
}
