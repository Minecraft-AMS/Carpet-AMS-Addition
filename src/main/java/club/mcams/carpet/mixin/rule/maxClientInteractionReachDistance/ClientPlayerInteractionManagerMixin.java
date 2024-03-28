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

package club.mcams.carpet.mixin.rule.maxClientInteractionReachDistance;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

//#if MC<12005
import club.mcams.carpet.AmsServerSettings;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.MinecraftClient;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.network.ClientPlayerInteractionManager;
//#else
//$$ import club.mcams.carpet.utils.compat.DummyClass;
//#endif

import org.spongepowered.asm.mixin.Mixin;

@GameVersion(version = "Minecraft < 1.20.5")
//#if MC<12005
@Mixin(ClientPlayerInteractionManager.class)
//#else
//$$ @Mixin(DummyClass.class)
//#endif
public abstract class ClientPlayerInteractionManagerMixin {
//#if MC<12005

    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyReturnValue(method = "getReachDistance()F", at = @At("RETURN"))
    private float getReachDistance(final float original) {
        if (AmsServerSettings.maxClientInteractionReachDistance != -1.0D && this.client.player != null) {
            return (float) AmsServerSettings.maxClientInteractionReachDistance;
        } else {
            return original;
        }
    }

//#endif
}
