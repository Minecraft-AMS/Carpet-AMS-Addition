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

package club.mcams.carpet.mixin.network;

import club.mcams.carpet.network.handler.C2SPayloadHandlerFactory;
import club.mcams.carpet.network.payload.AMS_CustomPayload;

import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onCustomPayload(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        //#if MC>=12005
        //$$ if (packet.payload() instanceof AMS_CustomPayload && packet.payload().getId().id().equals(AMS_CustomPayload.CHANNEL_ID)) {
        //$$     AMS_CustomPayload payload = (AMS_CustomPayload) packet.payload();
        //$$     if (C2SPayloadHandlerFactory.HANDLER_CHAIN.handle(payload)) {
        //$$         ci.cancel();
        //$$     }
        //$$ }
        //#else
        if (((CustomPayloadC2SPacketAccessor) packet).getChannel().equals(AMS_CustomPayload.CHANNEL_ID)) {
            AMS_CustomPayload payload = AMS_CustomPayload.decode(packet);
            if (C2SPayloadHandlerFactory.HANDLER_CHAIN.handle(payload)) {
                ci.cancel();
            }
        }
        //#endif
    }
}
