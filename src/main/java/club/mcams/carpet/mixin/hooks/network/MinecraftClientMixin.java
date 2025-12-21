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

package club.mcams.carpet.mixin.hooks.network;

import club.mcams.carpet.AmsClient;

import net.minecraft.client.MinecraftClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(
        //#if MC>=12111
        //$$ method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;ZZ)V",
        //#elseif MC>=12109
        //$$ method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V",
        //#elseif MC>=12106
        //$$ method = "disconnect",
        //#elseif MC>=12006
        //$$ method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V",
        //#else
        method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V",
        //#endif
        at = @At("HEAD")
    )
    private void onDisconnect(CallbackInfo ci) {
        AmsClient.getInstance().onDisconnect();
    }
}
