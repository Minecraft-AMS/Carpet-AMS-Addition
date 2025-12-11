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

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin implements CraftingRecipe {
    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        DefaultedList<ItemStack> remainders = CraftingRecipe.super.getRecipeRemainders(input);
        if (AmsServerSettings.craftableCarvedPumpkin) {
            ShapelessRecipe recipe = (ShapelessRecipe) (Object) this;
            ItemStack result = recipe.craft(input, MinecraftServerUtil.getServer().getRegistryManager());
            if (result.getItem().equals(Items.CARVED_PUMPKIN)) {
                return this.handleRemainders(input, remainders);
            }
        }
        return remainders;
    }

    @Unique
    private DefaultedList<ItemStack> handleRemainders(CraftingRecipeInput input, DefaultedList<ItemStack> originalRemainders) {
        DefaultedList<ItemStack> newRemainders = DefaultedList.ofSize(originalRemainders.size(), ItemStack.EMPTY);

        for (int i = 0; i < originalRemainders.size(); i++) {
            newRemainders.set(i, originalRemainders.get(i).copy());
        }

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getStackInSlot(i);
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

        RegistryWrapper.WrapperLookup lookup = MinecraftServerUtil.getServer().getRegistryManager();
        RegistryEntry<Enchantment> unbreakingEntry = lookup.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING);
        int unbreakingLevel = EnchantmentHelper.getLevel(unbreakingEntry, shears);

        Random random = new Random();
        boolean shouldDamage = true;

        if (unbreakingLevel > 0) {
            float chance = (float) unbreakingLevel / (unbreakingLevel + 1);
            if (random.nextFloat() < chance) {
                shouldDamage = false;
            }
        }

        if (shouldDamage) {
            int newDamage = resultShears.getDamage() + 1;
            resultShears.setDamage(newDamage);
            if (newDamage >= resultShears.getMaxDamage()) {
                return ItemStack.EMPTY;
            }
        }

        return resultShears;
    }
}
