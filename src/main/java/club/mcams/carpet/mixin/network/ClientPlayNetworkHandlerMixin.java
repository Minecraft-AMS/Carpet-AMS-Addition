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

import club.mcams.carpet.network.AMS_CustomPayload;

import club.mcams.carpet.network.rule.commandCustomBlockHardness.CustomBlockHardnessPayload;
import net.minecraft.client.network.ClientPlayNetworkHandler;
//#if MC>=12005
//$$ import net.minecraft.network.packet.CustomPayload;
//#else
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onCustomPayload(
        //#if MC>=12005
        //$$ CustomPayload payload,
        //#else
        CustomPayloadS2CPacket packet,
        //#endif
        CallbackInfo ci
    ) {
        //#if MC>=12005
        //$$ if (payload.getId().id().equals(AMS_CustomPayload.CHANNEL_ID)) {
        //$$     AMS_CustomPayload amsPayload = (AMS_CustomPayload) payload;
        //$$     if (amsPayload instanceof CustomBlockHardnessPayload) {
        //$$         ((CustomBlockHardnessPayload) amsPayload).handle();
        //$$         ci.cancel();
        //$$     }
        //$$ }
        //#else
        if (packet.getChannel().equals(AMS_CustomPayload.CHANNEL_ID)) {
            AMS_CustomPayload payload = AMS_CustomPayload.decode(packet);
            if (payload instanceof CustomBlockHardnessPayload) {
                ((CustomBlockHardnessPayload) payload).handle();
                ci.cancel();
            }
        }
        //#endif
    }
}
