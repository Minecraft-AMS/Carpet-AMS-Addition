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

package carpetamsaddition.mixin.rule.realPlayerControlDisabled;

import carpet.commands.PlayerCommand;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.FakePlayerHelper;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.PlayerUtil;
import carpetamsaddition.utils.messenger.Messenger;

import com.llamalad7.mixinextras.sugar.Local;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerCommand.class)
public abstract class Carpet_PlayerCommandMixin {
    @Unique
    private static final Translator tr = new Translator("rule.realPlayerControlDisabled");

    @Inject(
        method = "cantManipulate",
        at = @At(
            value = "INVOKE",
            //#if MC>=12006
            target = "Lnet/minecraft/commands/CommandSourceStack;getPlayer()Lnet/minecraft/server/level/ServerPlayer;"
            //#else
            //$$ target = "Lnet/minecraft/commands/CommandSourceStack;getPlayerOrException()Lnet/minecraft/server/level/ServerPlayer;"
            //#endif
        ),
        cancellable = true
    )
    private static void realPlayerControlDisabled(CallbackInfoReturnable<Boolean> cir, @Local CommandSourceStack source) throws CommandSyntaxException {
        if (CarpetAMSAdditionSettings.realPlayerControlDisabled && source != null) {
            Player player = PlayerUtil.getServerPlayerEntity(source);
            if (!FakePlayerHelper.isFakePlayer(player)) {
                Messenger.tell(player, Messenger.f(tr.tr("blocked"), Layout.RED));
                cir.setReturnValue(true);
            }
        }
    }
}
