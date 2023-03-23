package club.mcams.carpet.mixin.rule.infiniteTrades;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOffer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantEntity.class)
public class MerchantEntityMixin {
    @Inject(method = "trade", at = @At("RETURN"), cancellable = true)
    public void trade(TradeOffer offer, CallbackInfo ci) {
        if (AmsServerSettings.infiniteTrades) {
            offer.resetUses();
            ci.cancel();
        }
    }
}
