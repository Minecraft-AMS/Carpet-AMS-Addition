/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package carpetamsaddition.mixin.rule.sneakToEatCake;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CakeBlock.class)
public abstract class CakeBlockMixin {
    @WrapOperation(
        //#if MC>=12005
        method = "useWithoutItem",
        //#else
        //$$ method = "use",
        //#endif
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/CakeBlock;eat(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/InteractionResult;"
        )
    )
    private InteractionResult tryEat(
        //#if MC<11700
        //$$ CakeBlock cake,
        //#endif
        LevelAccessor world, BlockPos pos, BlockState state, Player player, Operation<InteractionResult> original
    ) {
        if (CarpetAMSAdditionSettings.sneakToEatCake && !player.isShiftKeyDown()) {
            return InteractionResult.FAIL;
        } else {
            //#if MC<11700
            //$$ return original.call(cake, world, pos, state, player);
            //#else
            return original.call(world, pos, state, player);
            //#endif
        }
    }
}
