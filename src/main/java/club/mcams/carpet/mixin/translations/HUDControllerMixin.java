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

import carpet.logging.HUDController;

import club.mcams.carpet.translations.AMSTranslations;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
//#if MC>=11900
//$$ import net.minecraft.text.Text;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HUDController.class)
public abstract class HUDControllerMixin {
    //#if MC>=11900
    //$$     @ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
    //$$   private static Text applyAMSTranslationToHudLoggerMessage(Text hudMessage, /* parent method parameters -> */ ServerPlayerEntity player, Text hudMessage_) {
    //$$        if (player != null) {
    //$$            hudMessage = AMSTranslations.translate((MutableText) hudMessage, player);
    //$$        }
    //$$        return hudMessage;
    //$$    }
    //#else
    @ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
    private static BaseText applyAMSTranslationToHudLoggerMessage(BaseText hudMessage, /* parent method parameters -> */ ServerPlayerEntity player, BaseText hudMessage_) {
        if (player != null) {
            hudMessage = AMSTranslations.translate(hudMessage, player);
        }
        return hudMessage;
    }
    //#endif
}
