/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.mixin.rule.powerfulExpMending;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {
    @Shadow
    private int value;

    @Shadow
    protected abstract int durabilityToXp(int durability);

    @Shadow
    protected abstract int xpToDurability(int experience);

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void fixAllItems(Player player, CallbackInfo ci) {
        if (!CarpetAMSAdditionSettings.powerfulExpMending) {
            return;
        }

        List<ItemStack> repairList = new ArrayList<>();
        Inventory inventory = player.inventory;
        repairList.addAll(inventory.items);
        repairList.addAll(inventory.armor);
        repairList.addAll(inventory.offhand);
        Collections.shuffle(repairList);

        for (ItemStack stack : repairList) {
            if (this.value <= 0) {
                break;
            }
            if (stack.isDamaged() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
                int repairAmount = Math.min(this.xpToDurability(this.value), stack.getDamageValue());
                stack.setDamageValue(stack.getDamageValue() - repairAmount);
                this.value -= this.durabilityToXp(repairAmount);
            }
        }
    }
}
