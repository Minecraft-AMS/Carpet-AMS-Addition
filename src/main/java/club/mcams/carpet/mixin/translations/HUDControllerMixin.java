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

import carpet.logging.HUDController;

import club.mcams.carpet.translations.AMSTranslations;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.19")
@Mixin(HUDController.class)
public abstract class HUDControllerMixin {
    @ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false, name = "arg1")
    private static BaseText applyAMSTranslationToHudLoggerMessage(BaseText hudMessage, ServerPlayerEntity player, BaseText hudMessage_) {
        if (player != null) {
            hudMessage = AMSTranslations.translate(hudMessage, player);
        }

        return hudMessage;
    }
}
