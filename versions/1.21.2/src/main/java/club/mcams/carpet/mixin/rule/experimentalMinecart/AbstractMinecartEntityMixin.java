/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21.2")
@SuppressWarnings("SimplifiableConditionalExpression")
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin {

    @Mutable
    @Shadow
    @Final
    private MinecartController controller;

    @Shadow
    protected abstract void moveOnRail(ServerWorld world);

    @ModifyReturnValue(method = "areMinecartImprovementsEnabled", at = @At("RETURN"))
    private static boolean setExMinecartEnabled(boolean original) {
        return AmsServerSettings.minecartMaxSpeed != -1.0D ? true : original;
    }

    @Unique
    private boolean isExperimental$AMS = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkAndUpdateController(CallbackInfo ci) {
        AbstractMinecartEntity self = (AbstractMinecartEntity) (Object) this;
        boolean shouldUseExperimental = AbstractMinecartEntity.areMinecartImprovementsEnabled(self.getWorld());

        if (shouldUseExperimental != isExperimental$AMS) {
            isExperimental$AMS = shouldUseExperimental;

            if (shouldUseExperimental) {
                ExperimentalMinecartController newController = new ExperimentalMinecartController(self);
                BlockPos blockPos = self.getRailOrMinecartPos();
                BlockState blockState = self.getWorld().getBlockState(blockPos);
                newController.adjustToRail(blockPos, blockState, true);
                this.controller = newController;
            }
            else {
                this.controller = new DefaultMinecartController(self);
            }
        }
    }
}
