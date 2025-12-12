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

package carpetamsaddition.mixin.rule.preventEndSpikeRespawn;

import carpetamsaddition.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(targets = "net/minecraft/world/level/dimension/end/DragonRespawnAnimation$3")
public abstract class EnderDragonSpawnStateMixin {
    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
        )
    )
    private boolean onRemoveBlock(ServerLevel serverWorld, BlockPos blockPos, boolean b, Operation<Boolean> original) {
        return Objects.equals(AmsServerSettings.preventEndSpikeRespawn, "false") ? original.call(serverWorld, blockPos, b) : false;
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
        )
    )
    private void onCreateExplosion(
        ServerLevel serverWorld,
        Entity entity,
        double x, double y, double z, float power,
        Level.ExplosionInteraction destructionType,
        Operation<Void> original
    ) {
        if (Objects.equals(AmsServerSettings.preventEndSpikeRespawn, "false")) {
            original.call(serverWorld, entity, x, y, z, power, destructionType);
        } else {
            serverWorld.explode(null, 0, 0, 0, 0.0F, Level.ExplosionInteraction.NONE);
        }
    }
}
