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

package carpetamsaddition.mixin.rule.safeFlight_invulnerable;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.damagesource.DamageSource;
//#if MC>=11904
import net.minecraft.world.damagesource.DamageTypes;
//#endif
import net.minecraft.server.level.ServerPlayer;
//#if MC>12006
import net.minecraft.server.level.ServerLevel;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "isInvulnerableTo",at = @At("TAIL"), cancellable = true)
    private void isInvulnerableTo(
        //#if MC>12006
        ServerLevel world,
        //#endif
        DamageSource damageSource, CallbackInfoReturnable<Boolean> cir
    ) {
        //#if MC>=11904
        if (CarpetAMSAdditionSettings.safeFlight && damageSource.is(DamageTypes.FLY_INTO_WALL)) {
        //#else
        //$$ if (CarpetAMSAdditionSettings.safeFlight && damageSource.equals(DamageSource.FLY_INTO_WALL)) {
        //#endif
            cir.setReturnValue(true);
            cir.cancel();
        }

        //#if MC>=11904
        if (CarpetAMSAdditionSettings.invulnerable && !damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {
        //#else
        //$$ if (CarpetAMSAdditionSettings.invulnerable && !damageSource.equals(DamageSource.OUT_OF_WORLD)) {
        //#endif
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
