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


package club.mcams.carpet.mixin.rule.bedRockFlying;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements ClientPlayerEntityInvoker {
    @Inject(method = "move", at = @At("TAIL"),cancellable = true)
    private void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if (AmsServerSettings.bedRockFlying) {
            ClientPlayerEntity currentInstance = (ClientPlayerEntity) (Object) this;
            if (
                movementType == MovementType.SELF &&
                //#if MC>=11700
                currentInstance.getAbilities().flying &&
                //#else if MC<11700
                //$$ currentInstance.abilities.flying &&
                //#endif
                !this.invokerHasMovementInput()) {
                currentInstance.setVelocity(Vec3d.ZERO);
                ci.cancel();
            }
        }
    }
}