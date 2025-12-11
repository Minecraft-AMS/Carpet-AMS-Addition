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

package club.mcams.carpet.mixin.rule.powerfulExpMending;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.*;

@GameVersion(version = "Minecraft >= 1.21")
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {
    @Inject(method = "repairPlayerGears", at = @At("HEAD"), cancellable = true)
    private void fixAllItems(ServerPlayerEntity player, int amount, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.powerfulExpMending) {
            int remaining = amount;
            List<ItemStack> repairList = new ArrayList<>();
            Inventory inventory = player.getInventory();
            for (int slot = 0; slot < inventory.size(); slot++) {
                ItemStack stack = inventory.getStack(slot);
                if (hasMendingEnchantment(stack)) {
                    repairList.add(stack);
                }
            }

            Collections.shuffle(repairList);

            for (ItemStack stack : repairList) {
                if (remaining <= 0) {
                    break;
                }
                int maxRepair = EnchantmentHelper.getRepairWithExperience(player.getEntityWorld(), stack, remaining);
                int actualRepair = Math.min(maxRepair, stack.getDamage());
                if (actualRepair > 0) {
                    stack.setDamage(stack.getDamage() - actualRepair);
                    remaining -= (actualRepair * remaining) / maxRepair;
                }
            }

            cir.setReturnValue(Math.max(remaining, 0));
            cir.cancel();
        }
    }

    @Unique
    public boolean hasMendingEnchantment(ItemStack stack) {
        Set<RegistryEntry<Enchantment>> entries = stack.getEnchantments().getEnchantments();
        RegistryKey<Enchantment> mendingKey = Enchantments.MENDING;

        for (RegistryEntry<Enchantment> entry : entries) {
            if (entry.matchesKey(mendingKey)) {
                return true;
            }
        }

        return false;
    }
}