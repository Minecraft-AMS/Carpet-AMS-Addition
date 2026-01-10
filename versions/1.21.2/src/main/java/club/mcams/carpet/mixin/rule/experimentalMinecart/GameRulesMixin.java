/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.world.GameRules;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameRules.class, priority = 168)
public abstract class GameRulesMixin {
    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/resource/featuretoggle/FeatureFlags;MINECART_IMPROVEMENTS:Lnet/minecraft/resource/featuretoggle/FeatureFlag;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static FeatureFlag beVanilla(Operation<FeatureFlag> original) {
        return FeatureFlags.VANILLA;
    }
}
