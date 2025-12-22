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

package carpetamsaddition.mixin.translations;

import carpetamsaddition.translations.AMSTranslations;
import carpetamsaddition.translations.ServerPlayerEntityWithClientLanguage;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements ServerPlayerEntityWithClientLanguage {
    @Unique
    private String clientLanguage$AMS = "en_US";

    @Inject(method = "updateOptions", at = @At("HEAD"))
    private void getClientLanguage(ClientInformation information, CallbackInfo ci) {
        this.clientLanguage$AMS = information.language();
    }

    @Override
    public String getClientLanguage$AMS() {
        return this.clientLanguage$AMS;
    }

    @ModifyVariable(method = "sendSystemMessage(Lnet/minecraft/network/chat/Component;Z)V", at = @At("HEAD"), argsOnly = true)
    private Component applyAMSTranslationToSystemMessage(Component message) {
        if (message instanceof MutableComponent) {
            message = AMSTranslations.translate((MutableComponent) message, (ServerPlayer) (Object) this);
        }

        return message;
    }
}
