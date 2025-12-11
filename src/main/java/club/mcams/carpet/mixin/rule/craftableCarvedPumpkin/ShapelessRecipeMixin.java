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

package club.mcams.carpet.mixin.rule.craftableCarvedPumpkin;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.MinecraftServerUtil;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin implements CraftingRecipe {
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = CraftingRecipe.super.getRemainingItems(input);
        if (AmsServerSettings.craftableCarvedPumpkin) {
            ShapelessRecipe recipe = (ShapelessRecipe) (Object) this;
            ItemStack result = recipe.assemble(input, MinecraftServerUtil.getServer().registryAccess());
            if (result.getItem().equals(Items.CARVED_PUMPKIN)) {
                return this.handleRemainders(input, remainders);
            }
        }
        return remainders;
    }

    @Unique
    private NonNullList<ItemStack> handleRemainders(CraftingInput input, NonNullList<ItemStack> originalRemainders) {
        NonNullList<ItemStack> newRemainders = NonNullList.withSize(originalRemainders.size(), ItemStack.EMPTY);

        for (int i = 0; i < originalRemainders.size(); i++) {
            newRemainders.set(i, originalRemainders.get(i).copy());
        }

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem().equals(Items.SHEARS)) {
                ItemStack shearsResult = this.processDurability(stack);
                if (!shearsResult.isEmpty()) {
                    newRemainders.set(i, shearsResult);
                }
                break;
            }
        }

        return newRemainders;
    }

    @Unique
    private ItemStack processDurability(ItemStack shears) {
        ItemStack resultShears = shears.copy();
        resultShears.setCount(1);

        HolderLookup.Provider lookup = MinecraftServerUtil.getServer().registryAccess();
        Holder<Enchantment> unbreakingEntry = lookup.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING);
        int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(unbreakingEntry, shears);

        Random random = new Random();
        boolean shouldDamage = true;

        if (unbreakingLevel > 0) {
            float chance = (float) unbreakingLevel / (unbreakingLevel + 1);
            if (random.nextFloat() < chance) {
                shouldDamage = false;
            }
        }

        if (shouldDamage) {
            int newDamage = resultShears.getDamageValue() + 1;
            resultShears.setDamageValue(newDamage);
            if (newDamage >= resultShears.getMaxDamage()) {
                return ItemStack.EMPTY;
            }
        }

        return resultShears;
    }
}
