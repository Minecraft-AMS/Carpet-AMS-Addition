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

package carpetamsaddition.mixin.rule.fakePlayerUseOfflinePlayerUuid;

import carpet.patches.EntityPlayerMPFake;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

//#if MC<12111
//$$ import com.mojang.authlib.GameProfile;
//#endif

//#if MC>=12111
import net.minecraft.server.MinecraftServer;
//#else
//$$ import net.minecraft.server.players.GameProfileCache;
//#endif

//#if MC>=11904
import net.minecraft.core.UUIDUtil;
//#else
//$$ import java.nio.charset.StandardCharsets;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC<12111
//$$ import java.util.Optional;
//#endif
import java.util.UUID;

@Mixin(EntityPlayerMPFake.class)
public abstract class Carpet_EntityPlayerMPFakeMixin {
    //#if MC>=12111
    @WrapOperation(
        method = "createFake",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/OldUsersConverter;convertMobOwnerIfNecessary(Lnet/minecraft/server/MinecraftServer;Ljava/lang/String;)Ljava/util/UUID;"
        )
    )
    private static UUID useOfflinePlayerUUID(MinecraftServer server, String playerName, Operation<UUID> original) {
        return
            CarpetAMSAdditionSettings.fakePlayerUseOfflinePlayerUUID ?
            UUIDUtil.createOfflinePlayerUUID(playerName) :
            original.call(server, playerName);
    }
    //#elseif MC>=11904
    //$$ @WrapOperation(
    //$$     method = "createFake",
    //$$     at = @At(
    //$$         value = "INVOKE",
    //$$         target = "Lnet/minecraft/server/players/GameProfileCache;get(Ljava/lang/String;)Ljava/util/Optional;"
    //$$     )
    //$$ )
    //$$ private static Optional<GameProfile> useOfflinePlayerUUID(GameProfileCache cache, String playerName, Operation<Optional<GameProfile>> original) {
    //$$     return
    //$$         CarpetAMSAdditionSettings.fakePlayerUseOfflinePlayerUUID ?
    //$$         Optional.of(new GameProfile(UUIDUtil.createOfflinePlayerUUID(playerName), playerName)) :
    //$$         original.call(cache, playerName);
    //$$ }
    //#else
    //$$ @WrapOperation(
    //$$     method = "createFake",
    //$$     at = @At(
    //$$         value = "INVOKE",
    //$$         target = "Lnet/minecraft/server/players/GameProfileCache;get(Ljava/lang/String;)Ljava/util/Optional;"
    //$$     )
    //$$ )
    //$$ private static Optional<GameProfile> useOfflinePlayerUUID(GameProfileCache cache, String playerName, Operation<Optional<GameProfile>> original) {
    //$$     return
    //$$         CarpetAMSAdditionSettings.fakePlayerUseOfflinePlayerUUID ?
    //$$         Optional.of(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)), playerName)) :
    //$$         original.call(cache, playerName);
    //$$ }
    //#endif
}
