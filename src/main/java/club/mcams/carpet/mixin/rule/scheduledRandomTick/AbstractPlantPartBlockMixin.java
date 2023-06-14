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

package club.mcams.carpet.mixin.rule.scheduledRandomTick;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.block.AbstractPlantPartBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC>=11900
//$$import net.minecraft.util.math.random.Random;
//#else
import java.util.Random;
//#endif

@Mixin(AbstractPlantPartBlock.class)
public abstract class AbstractPlantPartBlockMixin{
    @Inject(
            method = "scheduledTick",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/server/world/ServerWorld;breakBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
            ),
            cancellable = true
    )
    private void scheduleTickMixinInvoke(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (AmsServerSettings.scheduledRandomTickStem || AmsServerSettings.scheduledRandomTickAllPlants) {
            ci.cancel();
        }
    }

    @Inject(
            method = "scheduledTick",
            at = @At("TAIL")
    )
    private void scheduleTickMixinTail(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.getBlock() instanceof AbstractPlantStemBlock && (AmsServerSettings.scheduledRandomTickStem || AmsServerSettings.scheduledRandomTickAllPlants)) {
            state.randomTick(world, pos, random);
        }
    }
}
