
package club.mcams.carpet.mixin.rule.softBlock;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
	@Shadow
	public abstract Block getBlock();
	@Inject(method = "getHardness", at = @At("TAIL"), cancellable = true)
	public void getBlockHardness(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		//#if MC>=11700
		if(this.getBlock() == Blocks.DEEPSLATE && AmsServerSettings.softDeepslate) {
			cir.setReturnValue(1.5F);
		}
		//#endif
		if(this.getBlock() == Blocks.OBSIDIAN && AmsServerSettings.softObsidian) {
			cir.setReturnValue(3.0F);
		}
	}
}

