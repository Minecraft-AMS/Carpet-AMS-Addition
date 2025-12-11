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

package club.mcams.carpet.mixin.rule.onlyOpCanSpawnRealPlayerInWhitelist;

import carpet.commands.PlayerCommand;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.PlayerUtil;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.players.NameAndId;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerCommand.class)
public abstract class Carpet_PlayerCommandMixin {
    @ModifyExpressionValue(
        method = "cantSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;isUsingWhitelist()Z"
        )
    )
    private static boolean onlyOpCanSpawnRealPlayerInWhitelist(boolean isWhitelistEnabled, CommandContext<CommandSourceStack> context, @Local(name = "profile") NameAndId gameProfile) {
        if (AmsServerSettings.onlyOpCanSpawnRealPlayerInWhitelist && PlayerUtil.isInWhitelist(gameProfile)) {
            return isWhitelistEnabled || AmsServerSettings.onlyOpCanSpawnRealPlayerInWhitelist;
        } else {
            return isWhitelistEnabled;
        }
    }
}
