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

package carpetamsaddition.mixin.rule.shulkerGolem;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.helpers.ParticleHelper;

import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {
    @Inject(method = "trySpawnGolem", at = @At("TAIL"))
    private void trySpawnShulker(Level world, BlockPos headPos, CallbackInfo ci) {
        if (AmsServerSettings.shulkerGolem) {
            ServerLevel serverWorld = (ServerLevel) world;
            BlockPos bodyPos = headPos.below(1);
            boolean headIsCarvedPumpkin = world.getBlockState(headPos).getBlock() instanceof CarvedPumpkinBlock;
            boolean bodyIsShulkerBox = world.getBlockState(bodyPos).getBlock() instanceof ShulkerBoxBlock;
            if (headIsCarvedPumpkin && bodyIsShulkerBox) {
                Shulker shulkerGolem = EntityType.SHULKER.create(world, EntitySpawnReason.MOB_SUMMONED);
                Objects.requireNonNull(shulkerGolem).snapTo(bodyPos.getX() + 0.5, bodyPos.getY(), bodyPos.getZ() + 0.5, 0.0F, 0.0F);
                world.destroyBlock(bodyPos, false);
                world.destroyBlock(headPos, false);
                world.addFreshEntity(shulkerGolem);
                ParticleHelper.spawnShulkerGolemParticles(serverWorld, bodyPos);
            }
        }
    }
}
