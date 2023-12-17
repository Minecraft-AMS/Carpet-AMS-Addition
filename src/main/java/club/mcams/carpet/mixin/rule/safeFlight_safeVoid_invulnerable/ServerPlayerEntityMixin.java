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

package club.mcams.carpet.mixin.rule.safeFlight_safeVoid_invulnerable;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.entity.damage.DamageSource;
//#if MC>=11900
//$$ import net.minecraft.entity.damage.DamageTypes;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Inject(method = "isInvulnerableTo",at = @At("TAIL"), cancellable = true)
    private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        //#if MC>11900
        //$$ if (AmsServerSettings.safeFlight && damageSource.isOf(DamageTypes.FLY_INTO_WALL)) {
        //#else
        if (AmsServerSettings.safeFlight && damageSource.equals(DamageSource.FLY_INTO_WALL)) {
            //#endif
            cir.setReturnValue(true);
            cir.cancel();
        }
        //#if MC>11900
        //$$ if (AmsServerSettings.invulnerable && !damageSource.isOf(DamageTypes.OUT_OF_WORLD)) {
        //#else
        if (AmsServerSettings.invulnerable && !damageSource.equals(DamageSource.OUT_OF_WORLD)) {
            //#endif
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}