package club.mcams.carpet.mixin.rule.bambooModelNoOffset;

import club.mcams.carpet.AmsServerSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.block.AbstractBlock.AbstractBlockState.class)
public abstract class BambooBlockMixin {
    @Shadow public abstract Block getBlock();
    @Inject(method = "getModelOffset(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    public void getModelOffset(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> cir) {
        if(AmsServerSettings.bambooModelNoOffset && (this.getBlock() == Blocks.BAMBOO || this.getBlock() == Blocks.BAMBOO_SAPLING)) {
            cir.setReturnValue(Vec3d.ZERO);
            cir.cancel();
        }
    }
}