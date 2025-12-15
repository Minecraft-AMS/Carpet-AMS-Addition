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

package carpetamsaddition.mixin.rule.maxPlayerInteractionRange;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(value = AttributeInstance.class, priority = 1688)
public abstract class AttributeInstanceMixin implements AttributeInstanceInvoker {
    @ModifyReturnValue(method = "getBaseValue", at = @At("RETURN"))
    private double getPlayerBlockInteractionBaseValue(double original) {
        if (
            CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange != -1.0D &&
            Objects.equals(CarpetAMSAdditionSettings.maxPlayerBlockInteractionRangeScope, "global") &&
            this.invokeGetAttribute().equals(Attributes.BLOCK_INTERACTION_RANGE)
        ) {
            return CarpetAMSAdditionSettings.maxPlayerBlockInteractionRange;
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "getBaseValue", at = @At("RETURN"))
    private double getPlayerEntityInteractionBaseValue(double original) {
        if (
            CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange != -1.0D &&
            Objects.equals(CarpetAMSAdditionSettings.maxPlayerEntityInteractionRangeScope, "global") &&
            this.invokeGetAttribute().equals(Attributes.ENTITY_INTERACTION_RANGE)
        ) {
            return CarpetAMSAdditionSettings.maxPlayerEntityInteractionRange;
        } else {
            return original;
        }
    }
}
