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

package club.mcams.carpet.mixin.translations;

import club.mcams.carpet.translations.AMSTranslations;
import club.mcams.carpet.translations.ServerPlayerEntityWithClientLanguage;

//#if MC>=12002
//$$ import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
//#else
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityWithClientLanguage {
    @Unique
    private String clientLanguage$AMS = "en_US";

    @Inject(
        //#if MC>=12002
        //$$ method = "setClientOptions",
        //#else
        method = "setClientSettings",
        //#endif
        at = @At("HEAD")
    )
    private void recordClientLanguage(
        //#if MC>=12002
        //$$ SyncedClientOptions settings,
        //#else
        ClientSettingsC2SPacket packet,
        //#endif
        CallbackInfo ci
    )
    {
        this.clientLanguage$AMS =
            //#if MC>=12002
            //$$ settings.language();
            //#elseif MC>=11800
            packet.language();
            //#else
            //$$ ((ClientSettingsC2SPacketAccessor) packet).getLanguage$AMS();
            //#endif
    }

    @Override
    public String getClientLanguage$AMS() {
        return this.clientLanguage$AMS;
    }

    @ModifyVariable(
        method = {
            //#if MC>=11901
            //$$ "sendMessageToClient",
            //#elseif MC>=11600
            "sendMessage(Lnet/minecraft/text/Text;Z)V",
            "sendMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V",
            //#else
            //$$ "addChatMessage",
            //$$ "sendChatMessage",
            //#endif
        },
        at = @At("HEAD"),
        argsOnly = true
    )
    private Text applyAMSTranslationToSystemMessage(Text message) {
        if (message instanceof BaseText) {
            message = AMSTranslations.translate((BaseText) message, (ServerPlayerEntity) (Object) this);
        }

        return message;
    }
}
