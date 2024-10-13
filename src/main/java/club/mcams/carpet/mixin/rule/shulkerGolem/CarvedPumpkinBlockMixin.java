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

package club.mcams.carpet.mixin.rule.shulkerGolem;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.ParticleHelper;

import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.EntityType;
//#if MC>=12102
//$$ import net.minecraft.entity.SpawnReason;
//#endif
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {
    @Inject(method = "trySpawnEntity", at = @At("TAIL"))
    private void trySpawnShulker(World world, BlockPos headPos, CallbackInfo ci) {
        if (AmsServerSettings.shulkerGolem) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos bodyPos = headPos.down(1);
            boolean headIsCarvedPumpkin = world.getBlockState(headPos).getBlock() instanceof CarvedPumpkinBlock;
            boolean bodyIsShulkerBox = world.getBlockState(bodyPos).getBlock() instanceof ShulkerBoxBlock;
            if (headIsCarvedPumpkin && bodyIsShulkerBox) {
                //#if MC>=12102
                //$$ ShulkerEntity shulkerGolem = EntityType.SHULKER.create(world, SpawnReason.MOB_SUMMONED);
                //#else
                ShulkerEntity shulkerGolem = EntityType.SHULKER.create(world);
                //#endif
                Objects.requireNonNull(shulkerGolem).refreshPositionAndAngles(
                    bodyPos.getX() + 0.5, bodyPos.getY(), bodyPos.getZ() + 0.5, 0.0F, 0.0F
                );
                world.breakBlock(bodyPos, false);
                world.breakBlock(headPos, false);
                world.spawnEntity(shulkerGolem);
                ParticleHelper.spawnShulkerGolemParticles(serverWorld, bodyPos);
            }
        }
    }
}
