package club.mcams.carpet.mixin.rule.infiniteTrades;

import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffer.class)
public interface TradeOfferAccessor {
    @Accessor("uses")
    void setUses(int size);
}
