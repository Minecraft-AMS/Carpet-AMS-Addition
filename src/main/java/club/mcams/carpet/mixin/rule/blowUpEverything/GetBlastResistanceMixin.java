
package club.mcams.carpet.mixin.rule.blowUpEverything;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class GetBlastResistanceMixin {
    @Inject(method = "getBlastResistance", at = @At("TAIL"), cancellable = true)
    public void getBlastResistance(CallbackInfoReturnable<Float> cir) {
        if(AmsServerSettings.blowUpEverything){
            cir.setReturnValue(0.0F);
        }
    }
}