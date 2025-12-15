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

package carpetamsaddition.mixin.rule.flippinCactusSoundEffect;

import carpet.helpers.BlockRotator;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.EntityUtil;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRotator.class)
public abstract class Carpet_BlockRotatorMixin {
    @Inject(
        method = "flipBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;setValue(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;"
        )
    )
    private static void flipBlock(BlockState state, Level world, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAMSAdditionSettings.flippinCactusSoundEffect != 0) {
            float volume, pitch;
            SoundEvent leverClick = SoundEvents.LEVER_CLICK;
            SoundEvent villagerAmbient = SoundEvents.VILLAGER_AMBIENT;
            SoundEvent endermanTeleport = SoundEvents.ENDERMAN_TELEPORT;
            switch (CarpetAMSAdditionSettings.flippinCactusSoundEffect) {
                case 1:
                    volume = 1.0F;
                    pitch = 0.95F;
                    EntityUtil.getEntityWorld(player).playSound(null, player.blockPosition(), leverClick, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 2:
                    volume = 1.0F;
                    pitch = 2.5F;
                    EntityUtil.getEntityWorld(player).playSound(null, player.blockPosition(), leverClick, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 3:
                    volume = 1.0F;
                    pitch = 0.75F;
                    EntityUtil.getEntityWorld(player).playSound(null, player.blockPosition(), leverClick, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 4:
                    volume = 1.0F;
                    pitch = 1.0F;
                    EntityUtil.getEntityWorld(player).playSound(null, player.blockPosition(), villagerAmbient, SoundSource.BLOCKS, volume, pitch);
                    break;
                case 5:
                    volume = 1.0F;
                    pitch = 1.0F;
                    EntityUtil.getEntityWorld(player).playSound(null, player.blockPosition(), endermanTeleport, SoundSource.BLOCKS, volume, pitch);
            }
        }
    }
}
