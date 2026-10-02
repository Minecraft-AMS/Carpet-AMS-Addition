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

package carpetamsaddition.mixin.rule.safePointedDripstone;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
//#if MC<=12006
//$$ import net.minecraft.world.damagesource.DamageSource;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PointedDripstoneBlock.class)
public abstract class PointedDripstoneBlockMixin {
    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void onLandedUpon(
            Level world, BlockState state, BlockPos pos, Entity entity,
            //#if MC>12006
            double fallDistance,
            //#else
            //$$ float fallDistance,
            //#endif
            CallbackInfo ci
    ) {
        if (CarpetAMSAdditionSettings.safePointedDripstone && entity instanceof Player) {
            //#if MC>=11904
            entity.causeFallDamage(fallDistance, 1.0F, world.damageSources().fall());
            //#else
            //$$ entity.causeFallDamage(fallDistance, 1.0F, DamageSource.FALL);
            //#endif
            ci.cancel();
        }
    }
}
