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

import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@GameVersion(version = "Minecraft <= 1.20.6")
@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {
    @Inject(method = "repairPlayerItems", at = @At("HEAD"), cancellable = true)
    private void fixAllItems(Player player, int amount, CallbackInfoReturnable<Integer> cir) {
        if (!CarpetAMSAdditionSettings.powerfulExpMending) {
            return;
        }

        int remaining = amount;
        List<ItemStack> repairList = new ArrayList<>();
        Container inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
                repairList.add(stack);
            }
        }

        Collections.shuffle(repairList);
        for (ItemStack stack : repairList) {
            if (remaining <= 0) {
                break;
            }

            int actualRepair = Math.min(remaining * 2, stack.getDamageValue());
            if (actualRepair > 0) {
                stack.setDamageValue(stack.getDamageValue() - actualRepair);
                remaining -= actualRepair / 2;
            }
        }

        cir.setReturnValue(Math.max(remaining, 0));
    }
}
