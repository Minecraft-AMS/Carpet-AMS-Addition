/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.mixin.rule.hopperSuctionDisabled;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.level.block.entity.HopperBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BooleanSupplier;

@SuppressWarnings("SimplifiableConditionalExpression")
@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @WrapOperation(
        method = "tryMoveItems",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/function/BooleanSupplier;getAsBoolean()Z"
        )
    )
    private static boolean redirectSuckInItems(BooleanSupplier boolSupplier, Operation<Boolean> original) {
        return CarpetAMSAdditionSettings.hopperSuctionDisabled ? false : original.call(boolSupplier);
    }
}
