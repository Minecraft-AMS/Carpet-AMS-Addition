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

package club.mcams.carpet.mixin.util.settings;

import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.CarpetUtil;

import net.minecraft.server.command.ServerCommandSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingsManager.class)
public abstract class Carpet_SettingsManagerMixin {
    @Shadow
    protected abstract int setDefault(ServerCommandSource source, ParsedRule<?> rule, String value);

    @Inject(method = "setRule", at = @At("RETURN"))
    private void isMustSetDefaultRule(ServerCommandSource source, ParsedRule<?> rule, String value, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.MUST_SET_DEFAULT_RULES.contains(CarpetUtil.getRuleName(rule))) {
            this.setDefault(source, rule, value);
        }
    }
}
