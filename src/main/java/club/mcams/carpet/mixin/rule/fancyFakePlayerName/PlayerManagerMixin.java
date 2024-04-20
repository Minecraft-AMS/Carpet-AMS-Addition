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
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.BotTeamController;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyNameHelper;

import net.minecraft.network.ClientConnection;
//#if MC>=12002
//$$ import net.minecraft.server.network.ConnectedClientData;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Shadow
    public abstract MinecraftServer getServer();

    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    //#if MC>=12002
    //$$ private void onPlayerConnects(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
    //#else
    private void onPlayerConnects(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
    //#endif
        if (AmsServerSettings.fancyFakePlayerName && player instanceof EntityPlayerMPFake && !((EntityPlayerMPFake) player).isAShadow) {
            FancyNameHelper.addBotTeamNamePrefix(player);
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void remove(ServerPlayerEntity player, CallbackInfo info) {
        if (AmsServerSettings.fancyFakePlayerName && player instanceof EntityPlayerMPFake && !((EntityPlayerMPFake) player).isAShadow) {
            BotTeamController.kickFakePlayerFromBotTeam(player);
        }
        if (!AmsServerSettings.fancyFakePlayerName && player instanceof EntityPlayerMPFake && !((EntityPlayerMPFake) player).isAShadow) {
            BotTeamController.removeBotTeam(player);
        }
    }
}