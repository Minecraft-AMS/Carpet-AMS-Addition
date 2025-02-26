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

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@GameVersion(version = "Minecraft < 1.17")
@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {
    @Shadow
    private int amount;

    @Shadow
    protected abstract int getMendingRepairAmount(int experienceAmount);

    @Shadow
    protected abstract int getMendingRepairCost(int repairAmount);

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void fixAllItem(PlayerEntity player, CallbackInfo ci) {
        if (AmsServerSettings.powerfulExpMending) {
            List<ItemStack> repairCandidates = new ArrayList<>();
            PlayerInventory inventory = player.inventory;
            repairCandidates.addAll(inventory.main);
            repairCandidates.addAll(inventory.armor);
            repairCandidates.addAll(inventory.offHand);
            Collections.shuffle(repairCandidates);
            for (ItemStack stack : repairCandidates) {
                if (this.amount <= 0) {
                    break;
                }
                if (stack.isDamaged() && EnchantmentHelper.getLevel(Enchantments.MENDING, stack) > 0) {
                    int repairable = Math.min(this.getMendingRepairAmount(this.amount), stack.getDamage());
                    stack.setDamage(stack.getDamage() - repairable);
                    this.amount -= this.getMendingRepairCost(repairable);
                }
            }
        }
    }
}
