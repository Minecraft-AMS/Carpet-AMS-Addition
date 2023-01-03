package club.mcams.carpet.mixin.rule.netherWaterPlacement;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.world.dimension.DimensionType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public abstract class NetherWaterPlacementMixin {
    @Inject(method = "isUltrawarm", at = @At("TAIL"), cancellable = true)
    public void isUltrawarm(CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.netherWaterPlacement) {
            cir.setReturnValue(false);
        }
    }
}
