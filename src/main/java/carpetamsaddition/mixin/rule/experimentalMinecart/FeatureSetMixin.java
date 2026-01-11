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

package carpetamsaddition.mixin.rule.experimentalMinecart;

import carpetamsaddition.CarpetAMSAdditionLazySettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FeatureFlagSet.class, priority = 168)
public abstract class FeatureSetMixin {
    @ModifyReturnValue(method = "of(Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/flag/FeatureFlagSet;", at = @At("RETURN"))
    private static FeatureFlagSet beVanilla(FeatureFlagSet original, @Local(argsOnly = true) FeatureFlag flag) {
        if (CarpetAMSAdditionLazySettings.isEnabled(CarpetAMSAdditionLazySettings.Rule.EXPERIMENTAL_MINECART_ENABLED) && flag.equals(FeatureFlags.MINECART_IMPROVEMENTS)) {
            return FeatureFlagSet.of(FeatureFlags.VANILLA);
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "contains", at = @At("RETURN"))
    private boolean includeExperimentalMinecart(boolean original, @Local(argsOnly = true) FeatureFlag flag) {
        if (CarpetAMSAdditionLazySettings.isEnabled(CarpetAMSAdditionLazySettings.Rule.EXPERIMENTAL_MINECART_ENABLED) && flag.equals(FeatureFlags.MINECART_IMPROVEMENTS)) {
            return true;
        } else {
            return original;
        }
    }
}
