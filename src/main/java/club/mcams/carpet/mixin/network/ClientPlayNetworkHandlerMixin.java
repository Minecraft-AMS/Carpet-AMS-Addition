/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 */

package club.mcams.carpet.mixin.network;

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
        //$$ CustomPayload packet,
        //#else
        CustomPayloadS2CPacket packet,
        //#endif
        CallbackInfo ci
    ) {
        if (
            //#if MC>=12005
            //$$ packet.getId().id().equals(CustomBlockHardnessPayload.ID)
            //#else
            packet.getChannel().equals(CustomBlockHardnessPayload.ID)
            //#endif
        ) {
            CustomBlockHardnessPayload.handleCustomBlockHardnessPacket(packet);
            ci.cancel();
        }
    }
}