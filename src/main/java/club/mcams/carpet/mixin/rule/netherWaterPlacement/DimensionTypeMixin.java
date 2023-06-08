package club.mcams.carpet.mixin.rule.netherWaterPlacement;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.world.dimension.DimensionType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
    //#if MC>=12000
    //$$ @Inject(method = "ultrawarm", at = @At("TAIL"), cancellable = true)
    //#else
    @Inject(method = "isUltrawarm", at = @At("TAIL"), cancellable = true)
    //#endif
    public void isUltrawarm(CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.netherWaterPlacement) {
            cir.setReturnValue(false);
        }
    }
}
