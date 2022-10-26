package club.mcams.carpet.mixin.survival;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.entity.EquipmentSlot;

import club.mcams.carpet.AmsServerSettings;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(InfinityEnchantment.class)
public class SuperBow extends Enchantment {
    public SuperBow(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.BOW, slotTypes);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        if(AmsServerSettings.SuperBow){
            return other instanceof MendingEnchantment || super.canAccept(other);
        }
        return false;
    }
}
