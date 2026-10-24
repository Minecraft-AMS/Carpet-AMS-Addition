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

package carpetamsaddition.mixin.network;

import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.utils.NetworkUtil;

//#if MC>=12005
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//#else
//$$ import net.minecraft.client.multiplayer.ClientPacketListener;
//$$ import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
    //#if MC>=12005
    ClientCommonPacketListenerImpl.class
    //#else
    //$$ ClientPacketListener.class
    //#endif
)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(
        //#if MC>=12005
        method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
        //#else
        //$$ method = "handleCustomPayload",
        //#endif
        at = @At("HEAD"),
        cancellable = true
    )
    private void onCustomPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (!AMS_CustomPayload.CHANNEL_ID.equals(NetworkUtil.getChannelId(packet))) {
            return;
        }

        try {
            AMS_CustomPayload payload = NetworkUtil.getAmsPayload(packet);
            if (payload != null && AMS_PayloadManager.DispatcherGetter.getS2CDispatcher().dispatch(payload)) {
                ci.cancel();
            }
        } finally {
            NetworkUtil.releasePayload(packet);
        }
    }
}
