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

package carpetamsaddition.mixin.rule.redstoneComponentSound;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepeaterBlock.class)
public abstract class RepeaterBlockMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"))
    private void playSound(
        BlockState state, Level world, BlockPos pos, Player player,
        BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (CarpetAMSAdditionSettings.redstoneComponentSound) {
            world.playSound(player, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0f, 0.75f);
        }
    }
}