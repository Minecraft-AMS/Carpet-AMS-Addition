package club.mcams.carpet.mixin.rule.fakePeace;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();
    @Inject(method = "allowsSpawning", at = @At("TAIL"), cancellable = true)
    private void allowsSpawning(BlockView world, BlockPos pos, EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.fakePeace && getBlock() != Blocks.VOID_AIR) {
            cir.setReturnValue(false);
        }
    }
}
