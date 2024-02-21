/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.fakePlayerInNetworkUpdateStage;

import carpet.helpers.EntityPlayerActionPack;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.fakePlayerInNetworkUpdateStage.IEntityPlayerActionPack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerActionPack.class, remap = false)
public class MixinEntityPlayerActionPack implements IEntityPlayerActionPack {
    @Unique
    boolean isNetworkPhase = false;

    @Inject(
            method = "onUpdate",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onUpdate(CallbackInfo ci) {
        if (AmsServerSettings.fakePlayerInNetworkUpdateStage && !isNetworkPhase) {
            ci.cancel();
        }
    }

    @Override
    public void setNetworkPhase$AMS(boolean networkPhase) {
        isNetworkPhase = networkPhase;
    }

    @Override
    public boolean isNetworkPhase$AMS() {
        return isNetworkPhase;
    }
}
