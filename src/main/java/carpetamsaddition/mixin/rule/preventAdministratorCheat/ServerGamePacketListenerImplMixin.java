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

package carpetamsaddition.mixin.rule.preventAdministratorCheat;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.preventAdministratorCheat.PermissionHelper;
import carpetamsaddition.utils.Noop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft > 1.20.6")
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @WrapOperation(
        method = "handleChangeGameMode",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/commands/GameModeCommand;setGameMode(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/GameType;)V"
        )
    )
    private void preventCheat(ServerPlayer player, GameType type, Operation<Void> original) {
        if (CarpetAMSAdditionSettings.preventAdministratorCheat && !PermissionHelper.canCheat(player.createCommandSourceStack())) {
            Noop.noop();
        } else {
            original.call(player, type);
        }
    }
}
