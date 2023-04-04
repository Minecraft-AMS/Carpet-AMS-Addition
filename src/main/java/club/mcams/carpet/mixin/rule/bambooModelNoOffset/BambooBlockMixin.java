package club.mcams.carpet.mixin.rule.bambooModelNoOffset;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BambooBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BambooBlock.class)
public abstract class BambooBlockMixin {
    @Inject(method = "getOffsetType", at = @At("TAIL"), cancellable = true)
    public void getOffsetType(CallbackInfoReturnable<AbstractBlock.OffsetType> cir) {
        if(AmsServerSettings.bambooModelNoOffset) {
            cir.setReturnValue(AbstractBlock.OffsetType.NONE);
            cir.cancel();
        }
    }
}
