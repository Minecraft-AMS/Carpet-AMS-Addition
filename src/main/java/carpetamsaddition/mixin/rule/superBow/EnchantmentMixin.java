/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package carpetamsaddition.mixin.rule.superBow;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.Set;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @ModifyReturnValue(method = "areCompatible", at = @At("RETURN"))
    private static boolean canBeCombined(boolean original, Holder<@NotNull Enchantment> first, Holder<@NotNull Enchantment> second) {
        return original || (CarpetAMSAdditionSettings.superBow && canBeCombinedEnchantments(first, second, Set.of(Enchantments.INFINITY, Enchantments.MENDING)));
    }

    @Unique
    private static boolean canBeCombinedEnchantments(Holder<@NotNull Enchantment> first, Holder<@NotNull Enchantment> second, Set<ResourceKey<@NotNull Enchantment>> enchantments) {
        boolean firstMatches = enchantments.stream().anyMatch(first::is);
        boolean secondMatches = enchantments.stream().anyMatch(second::is);
        Optional<ResourceKey<@NotNull Enchantment>> secondKeyOpt = second.unwrapKey();
        boolean notMatchSecond = true;

        if (secondKeyOpt.isPresent()) {
            notMatchSecond = !first.is(secondKeyOpt.get());
        }

        return firstMatches && secondMatches && notMatchSecond;
    }
}
