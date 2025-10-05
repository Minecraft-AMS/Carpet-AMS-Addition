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

import club.mcams.carpet.network.handler.S2CPayloadHandlerFactory;
import club.mcams.carpet.network.payload.AMS_CustomPayload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//#if MC>=12005
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import net.minecraft.client.network.ClientCommonNetworkHandler;
//$$ import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
//#else
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;
//#endif
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(
    //#if MC>=12005
    //$$ ClientCommonNetworkHandler.class
    //#else
    ClientPlayNetworkHandler.class
    //#endif
)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(
        //#if MC>=12005
        //$$ method = "onCustomPayload(Lnet/minecraft/network/packet/s2c/common/CustomPayloadS2CPacket;)V",
        //#else
        method = "onCustomPayload",
        //#endif
        at = @At("HEAD"),
        cancellable = true
    )
    private void onCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        //#if MC>=12005
        //$$ if (packet.payload() instanceof AMS_CustomPayload && packet.payload().getId().id().equals(AMS_CustomPayload.CHANNEL_ID)) {
        //$$     AMS_CustomPayload payload = (AMS_CustomPayload) packet.payload();
        //$$     if (S2CPayloadHandlerFactory.HANDLER_CHAIN.handle(payload)) {
        //$$         ci.cancel();
        //$$     }
        //$$ }
        //#else
        Identifier channel = ((CustomPayloadS2CPacketAccessor) packet).getChannel();
        if (channel.equals(AMS_CustomPayload.CHANNEL_ID)) {
            PacketByteBuf packetByteBuf = ((CustomPayloadS2CPacketAccessor) packet).getData();
            try {
                AMS_CustomPayload payload = AMS_CustomPayload.decode(packet);
                if (payload != null && S2CPayloadHandlerFactory.HANDLER_CHAIN.handle(payload)) {
                    ci.cancel();
                }
            } finally {
                packetByteBuf.release();
            }
        }
        //#endif
    }
}
