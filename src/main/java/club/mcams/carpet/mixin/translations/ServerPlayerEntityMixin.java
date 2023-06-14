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

package club.mcams.carpet.mixin.translations;

import club.mcams.carpet.translations.ServerPlayerEntityWithClientLanguage;

import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityWithClientLanguage {
    private String clientLanguage$AMS = "en_US";

    //#if MC>=11800
    @Inject(method = "setClientSettings", at = @At("HEAD"))
    private void recordClientLanguage(ClientSettingsC2SPacket packet, CallbackInfo ci) {
        this.clientLanguage$AMS = packet.language();
    }
    //#else
    //$$ @Inject(method = "setClientSettings", at = @At("HEAD"))
    //$$ private void recordClientLanguage(ClientSettingsC2SPacket packet, CallbackInfo ci) {
    //$$     this.clientLanguage$AMS = ((ClientSettingsC2SPacketAccessor) packet).getLanguage$AMS();
    //$$ }
    //#endif

    @Override
    public String getClientLanguage$AMS() {
        return this.clientLanguage$AMS;
    }
}
