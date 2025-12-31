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

package club.mcams.carpet.mixin.rule.experimentalMinecart;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FeatureSet.class)
public abstract class FeatureSetMixin {

    @Unique
    private static final boolean ENABLE_FLAG$AMS;

    static {
        ENABLE_FLAG$AMS = AmsServerSettings.experimentalMinecartEnabled;
    }

    @ModifyReturnValue(method = "contains", at = @At("RETURN"))
    private boolean contains(boolean original, @Local(argsOnly = true) FeatureFlag flag) {
        if (ENABLE_FLAG$AMS && flag.equals(FeatureFlags.MINECART_IMPROVEMENTS)) {
            return true;
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "of(Lnet/minecraft/resource/featuretoggle/FeatureFlag;)Lnet/minecraft/resource/featuretoggle/FeatureSet;", at = @At("RETURN"))
    private static FeatureSet noCheckFlag(FeatureSet original, @Local(argsOnly = true) FeatureFlag flag) {
        if (flag.equals(FeatureFlags.MINECART_IMPROVEMENTS)) {
            return FeatureSet.of(FeatureFlags.VANILLA);
        } else {
            return original;
        }
    }
}
