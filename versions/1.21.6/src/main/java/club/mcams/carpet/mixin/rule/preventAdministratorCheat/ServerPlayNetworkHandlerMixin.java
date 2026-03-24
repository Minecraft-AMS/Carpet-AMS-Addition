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

package club.mcams.carpet.mixin.rule.preventAdministratorCheat;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.preventAdministratorCheat.PermissionHelper;
import club.mcams.carpet.utils.Noop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21.6")
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @WrapOperation(
        method = "onChangeGameMode",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/command/GameModeCommand;execute(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/GameMode;)V"
        )
    )
    private void preventCheat(ServerPlayerEntity serverPlayerEntity, GameMode gameMode, Operation<Void> original) {
        if (AmsServerSettings.preventAdministratorCheat && !PermissionHelper.canCheat(serverPlayerEntity.getCommandSource())) {
            Noop.noop();
        } else {
            original.call(serverPlayerEntity, gameMode);
        }
    }
}
