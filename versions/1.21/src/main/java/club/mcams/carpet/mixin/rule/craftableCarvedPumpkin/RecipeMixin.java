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
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Random;

@GameVersion(version = "Minecraft < 1.21.2")
@SuppressWarnings("unchecked")
@Mixin(Recipe.class)
public interface RecipeMixin<T extends RecipeInput> {
    @Inject(method = "getRemainder", at = @At("RETURN"), cancellable = true)
    default void onGetRemainder(T input, CallbackInfoReturnable<DefaultedList<ItemStack>> cir) {
        if (AmsServerSettings.craftableCarvedPumpkin) {
            Recipe<T> recipe = (Recipe<T>) this;
            RegistryWrapper.WrapperLookup lookup = MinecraftServerUtil.getServer().getRegistryManager();
            ItemStack output = recipe.getResult(lookup);

            if (output != null && output.getItem().equals(Items.CARVED_PUMPKIN)) {
                DefaultedList<ItemStack> remainders = cir.getReturnValue();
                for (int i = 0; i < input.getSize(); i++) {

                    ItemStack stack = input.getStackInSlot(i);
                    RegistryEntry<Enchantment> unbreakingEntry = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING);

                    if (stack.getItem().equals(Items.SHEARS)) {
                        int unbreakingLevel = EnchantmentHelper.getLevel(unbreakingEntry, stack);
                        Random random = new Random();
                        boolean shouldDamage = true;

                        if (unbreakingLevel > 0) {
                            float chance = (float) unbreakingLevel / (unbreakingLevel + 1);
                            if (random.nextFloat() < chance) {
                                shouldDamage = false;
                            }
                        }

                        ItemStack resultShears = stack.copy();
                        resultShears.setCount(1);

                        if (shouldDamage) {
                            int newDamage = resultShears.getDamage() + 1;
                            resultShears.setDamage(newDamage);
                            if (newDamage >= resultShears.getMaxDamage()) {
                                remainders.set(i, ItemStack.EMPTY);
                            } else {
                                remainders.set(i, resultShears);
                            }
                        } else {
                            remainders.set(i, resultShears);
                        }

                        break;
                    }
                }
                cir.setReturnValue(remainders);
            }
        }
    }
}
