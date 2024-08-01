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

package club.mcams.carpet.mixin.rule.fancyFakePlayerName;

import carpet.patches.EntityPlayerMPFake;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.FakePlayerHelper;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyFakePlayerNameTeamController;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyNameHelper;

import net.minecraft.network.ClientConnection;
//#if MC>=12002
//$$ import net.minecraft.server.network.ConnectedClientData;
//#endif
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void onPlayerConnects(
        ClientConnection connection,
        ServerPlayerEntity player,
        //#if MC>=12002
        //$$ ConnectedClientData clientData,
        //#endif
        CallbackInfo ci
    ) {
        if (
            !Objects.equals(AmsServerSettings.fancyFakePlayerName, "false") &&
            FakePlayerHelper.isFakePlayer(player) &&
            !((EntityPlayerMPFake) player).isAShadow
        ) {
            FancyNameHelper.addBotTeamNamePrefix(player, AmsServerSettings.fancyFakePlayerName);
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void kickFakePlayerFromBotTeam(ServerPlayerEntity player, CallbackInfo info) {
        if (
            !Objects.equals(AmsServerSettings.fancyFakePlayerName, "false") &&
            FakePlayerHelper.isFakePlayer(player) &&
            !((EntityPlayerMPFake) player).isAShadow
        ) {
            FancyFakePlayerNameTeamController.kickFakePlayerFromBotTeam(player, AmsServerSettings.fancyFakePlayerName);
        }
    }
}