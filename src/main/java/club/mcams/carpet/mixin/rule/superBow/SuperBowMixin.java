package club.mcams.carpet.mixin.rule.superBow;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.entity.EquipmentSlot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InfinityEnchantment.class)
public abstract class SuperBowMixin extends Enchantment {
    public SuperBowMixin(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.BOW, slotTypes);
    }
    @Inject(method = "canAccept", at = @At("HEAD"), cancellable = true)
    public void canAccept(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.superBow) {
            cir.setReturnValue(other instanceof MendingEnchantment || super.canAccept(other));
        }
    }
}
