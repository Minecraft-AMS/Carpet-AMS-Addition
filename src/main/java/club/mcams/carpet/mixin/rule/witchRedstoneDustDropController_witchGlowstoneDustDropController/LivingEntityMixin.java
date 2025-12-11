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

package club.mcams.carpet.mixin.rule.witchRedstoneDustDropController_witchGlowstoneDustDropController;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.EntityUtil;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "dropLoot(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;Z)V", at = @At("TAIL"))
    private void customRedstoneDustDrop(CallbackInfo ci) {
        if (AmsServerSettings.witchRedstoneDustDropController != -1) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            ServerWorld world = (ServerWorld) EntityUtil.getEntityWorld(livingEntity);
            int redstoneCount = AmsServerSettings.witchRedstoneDustDropController;
            compatWitchDropStack(Items.REDSTONE, redstoneCount, world, livingEntity);
        }
    }

    @Inject(method = "dropLoot(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;Z)V", at = @At("TAIL"))
    private void customGlowstoneDustDrop(CallbackInfo ci) {
        if (AmsServerSettings.witchGlowstoneDustDropController != -1) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            ServerWorld world = (ServerWorld) EntityUtil.getEntityWorld(livingEntity);
            int glowstoneCount = AmsServerSettings.witchGlowstoneDustDropController;
            compatWitchDropStack(Items.GLOWSTONE_DUST, glowstoneCount, world, livingEntity);
        }
    }

    @Unique
    private static void compatWitchDropStack(Item item, int count, ServerWorld world, LivingEntity livingEntity) {
        if (livingEntity instanceof WitchEntity) {
            livingEntity.dropStack(world, new ItemStack(item, count));
        }
    }
}
