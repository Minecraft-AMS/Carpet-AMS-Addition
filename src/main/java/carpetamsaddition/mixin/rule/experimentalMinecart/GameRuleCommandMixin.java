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

package carpetamsaddition.mixin.rule.experimentalMinecart;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.FeatureChecker;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.GameRuleCommand;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameRuleCommand.class, priority = 1024)
public abstract class GameRuleCommandMixin {
    @Unique
    private static final Translator tr = new Translator("rule.experimentalMinecartSpeed");

    @Inject(method = "setRule", at = @At("HEAD"), cancellable = true)
    private static void onSet(CommandContext<CommandSourceStack> context, GameRule<?> gameRule, CallbackInfoReturnable<Integer> cir) {
        if (CarpetAMSAdditionSettings.experimentalMinecartEnabled && CarpetAMSAdditionSettings.experimentalMinecartSpeed != -1.0D && gameRule.equals(GameRules.MAX_MINECART_SPEED)) {
            Messenger.tell(context.getSource(), Messenger.f(tr.tr("vanilla_command_disabled"), Layout.RED));
            cir.setReturnValue(0);
            cir.cancel();
        }

        if (!FeatureChecker.EX_MINECART_FEATURE.get() && gameRule.equals(GameRules.MAX_MINECART_SPEED)) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }
}
