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

import com.google.common.collect.Lists;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Collections;
import java.util.List;

@GameVersion(version = "Minecraft >= 1.17")
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {
    @Inject(method = "repairPlayerGears", at = @At("HEAD"), cancellable = true)
    private void fixAllItem(PlayerEntity player, int amount, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.powerfulExpMending) {
            List<ItemStack> allItems = Lists.newArrayList();
            allItems.addAll(player.getInventory().main);
            allItems.addAll(player.getInventory().armor);
            allItems.addAll(player.getInventory().offHand);
            Collections.shuffle(allItems);
            int remaining = amount;
            for (ItemStack stack : allItems) {
                if (!stack.isEmpty() && stack.isDamaged() && EnchantmentHelper.getLevel(Enchantments.MENDING, stack) > 0) {
                    int repairAmount = Math.min(remaining * 2, stack.getDamage());
                    stack.setDamage(stack.getDamage() - repairAmount);
                    remaining -= repairAmount / 2;
                    if (remaining <= 0) {
                        break;
                    }
                }
            }
            cir.setReturnValue(remaining);
            cir.cancel();
        }
    }
}
