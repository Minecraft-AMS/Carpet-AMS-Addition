/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.fakePlayerUseOfflinePlayerUuid;

import carpet.commands.PlayerCommand;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import com.mojang.authlib.GameProfile;

//#if MC>=11900
//$$ import net.minecraft.util.Uuids;
//#else
import net.minecraft.entity.player.PlayerEntity;
//#endif
import net.minecraft.util.UserCache;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Optional;

@GameVersion(version = "Minecraft < 1.21.9")
@Mixin(PlayerCommand.class)
public abstract class Carpet_PlayerCommandMixin {
    @WrapOperation(
        method = "cantSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/UserCache;findByName(Ljava/lang/String;)Ljava/util/Optional;"
        )
    )
    private static Optional<GameProfile> useOfflinePlayerUUID(UserCache userCache, String playerName, Operation<Optional<GameProfile>> original) {
        return
            AmsServerSettings.fakePlayerUseOfflinePlayerUUID ?
            //#if MC>=11900
            //$$ Optional.of(new GameProfile(Uuids.getOfflinePlayerUuid(playerName), playerName)) :
            //#else
            Optional.of(new GameProfile(PlayerEntity.getOfflinePlayerUuid(playerName), playerName)) :
            //#endif
            original.call(userCache, playerName);
    }
}
