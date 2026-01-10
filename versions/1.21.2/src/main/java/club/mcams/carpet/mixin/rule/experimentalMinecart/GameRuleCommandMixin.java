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

package club.mcams.carpet.mixin.rule.experimentalMinecart;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.FeatureChecker;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>=12111
//$$ import net.minecraft.world.rule.GameRule;
//#endif
import net.minecraft.world.GameRules;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21.2")
@Mixin(value = GameRuleCommand.class, priority = 168)
public abstract class GameRuleCommandMixin {
    @Unique
    private static final Translator tr = new Translator("rule.experimentalMinecartSpeed");

    @Inject(method = "executeSet", at = @At("HEAD"), cancellable = true)
    private static void onSet(
        CommandContext<ServerCommandSource> context,
        //#if MC>=12111
        //$$ GameRule<?> key,
        //#else
        GameRules.Key<?> key,
        //#endif
        CallbackInfoReturnable<Integer> cir
    ) {
        if (AmsServerSettings.experimentalMinecartEnabled && AmsServerSettings.experimentalMinecartSpeed != -1.0D && key.equals(GameRules.MINECART_MAX_SPEED)) {
            Messenger.tell(context.getSource(), Messenger.f(tr.tr("vanilla_command_disabled"), Layout.RED));
            cir.setReturnValue(0);
            cir.cancel();
        }

        if (!FeatureChecker.EX_MINECART_FEATURE.get()) {
            cir.cancel();
        }
    }
}
