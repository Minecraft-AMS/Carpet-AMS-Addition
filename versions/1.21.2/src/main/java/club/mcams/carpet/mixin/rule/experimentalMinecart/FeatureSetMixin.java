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
import club.mcams.carpet.AmsServerStaticSettings;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;

import org.spongepowered.asm.mixin.Mixin;

import static club.mcams.carpet.AmsServerStaticSettings.Rule.EXPERIMENTAL_MINECART_ENABLED;

@Mixin(FeatureSet.class)
public abstract class FeatureSetMixin {
    @WrapMethod(method = "of(Lnet/minecraft/resource/featuretoggle/FeatureFlag;)Lnet/minecraft/resource/featuretoggle/FeatureSet;")
    private static FeatureSet noCheckExperimentalMinecartFlag(FeatureFlag flag, Operation<FeatureSet> original) {
        if (flag.equals(FeatureFlags.MINECART_IMPROVEMENTS)) {
            return FeatureSet.of(FeatureFlags.VANILLA);
        } else {
            return original.call(flag);
        }
    }

    @WrapMethod(method = "contains")
    private boolean includeExperimentalMinecart(FeatureFlag flag, Operation<Boolean> original) {
        if (!AmsServerSettings.experimentalMinecartEnabled) {
            return original.call(flag);
        }

        if (AmsServerStaticSettings.isEnabled(EXPERIMENTAL_MINECART_ENABLED)) {
            return true;
        }

        return original.call(flag);
    }
}
