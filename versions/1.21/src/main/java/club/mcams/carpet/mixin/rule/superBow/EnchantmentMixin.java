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

package club.mcams.carpet.mixin.rule.superBow;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.item.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.entry.RegistryEntry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21")
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Inject(
        method = "canBeCombined",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/entry/RegistryEntry;equals(Ljava/lang/Object;)Z"
        ),
        cancellable = true, remap = false
    )
    private static void canBeCombined(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second, CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.superBow && first.getKey().isPresent() && second.getKey().isPresent()) {
            boolean isBow = first.value().isAcceptableItem(Items.BOW.getDefaultStack());
            boolean firstIsInfinity = first.getKey().get().equals(Enchantments.INFINITY) || second.getKey().get().equals(Enchantments.MENDING);
            boolean firstIsMending = first.getKey().get().equals(Enchantments.MENDING) || second.getKey().get().equals(Enchantments.INFINITY);
            if (isBow && (firstIsInfinity || firstIsMending)) {
                cir.setReturnValue(true);
            }
        }
    }
}
