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

package club.mcams.carpet.mixin.rule.phantomSpawnAlert;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Messenger;

import club.mcams.carpet.utils.PlayerUtil;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    @Unique
    private static final Translator translator = new Translator("rule.phantomSpawnAlert");

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/Phantom;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
            shift = At.Shift.AFTER
        )
    )
    private void broadcastMessage(ServerLevel world, boolean spawnMonsters, CallbackInfo ci, @Local ServerPlayer playerEntity, @Local Phantom phantom) {
        if (AmsServerSettings.phantomSpawnAlert && phantom != null) {
            MinecraftServer server = world.getServer();
            String playerName = PlayerUtil.getName(playerEntity);
            double x = phantom.getX();
            double y = phantom.getY();
            double z = phantom.getZ();
            String coord = x + ", " + y + ", " + z;
            Messenger.sendServerMessage(server, Messenger.s(translator.tr("msg", playerName, coord).getString()));
        }
    }
}