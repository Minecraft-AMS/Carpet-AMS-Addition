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

package carpetamsaddition.mixin.carpet;

import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;

import carpetamsaddition.AmsServer;
import carpetamsaddition.AmsServerMod;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Messenger;

import net.minecraft.commands.CommandSourceStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingsManager.class)
public abstract class SettingsManagerMixin {

    @Unique
    private static final Translator translator = new Translator("carpet.totalRules");

    @SuppressWarnings("ConstantValue")
    @Inject(
        method = "listAllSettings",
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=carpet.settings.command.version",
                ordinal = 0
            )
        ),
        at = @At(
            value = "INVOKE",
            target = "Lcarpet/api/settings/SettingsManager;getCategories()Ljava/lang/Iterable;",
            ordinal = 0
        ),
        remap = false
    )
    private void printVersion(CommandSourceStack source, CallbackInfoReturnable<Integer> cir) {
        if ((Object)this == CarpetServer.settingsManager) {
            Messenger.tell(
                source, Messenger.c(
                    String.format("g %s ", AmsServer.fancyName),
                    String.format("g %s: ", translator.tr("version").getString()),
                    String.format("g %s ", AmsServerMod.getVersion()),
                    String.format("g (%s: %d)", translator.tr("total_rules").getString(), AmsServer.ruleCount)
                )
            );
        }
    }
}
