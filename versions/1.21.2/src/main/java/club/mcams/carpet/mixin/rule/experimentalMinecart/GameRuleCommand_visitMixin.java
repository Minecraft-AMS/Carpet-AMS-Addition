/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.experimentalMinecart;

import club.mcams.carpet.helpers.FeatureChecker;

import net.minecraft.world.GameRules;
//#if MC>=12111
//$$ import net.minecraft.world.rule.GameRule;
//#endif

import org.jetbrains.annotations.NotNull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.command.GameRuleCommand$1", priority = 168)
public abstract class GameRuleCommand_visitMixin {
    @Inject(method = "visit", at = @At("HEAD"), cancellable = true)
    private void test(
        //#if MC>=12111
        //$$ @NotNull GameRule<?> rule,
        //#else
        @NotNull GameRules.Key<?> rule,
        GameRules.Type<?> type,
        //#endif
        CallbackInfo ci
    ) {
        if (rule.equals(GameRules.MINECART_MAX_SPEED) && !FeatureChecker.EX_MINECART_FEATURE.get()) {
            ci.cancel();
        }
    }
}
