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

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.mob.PhantomEntity;
//#if MC>=12006
//$$ import net.minecraft.server.network.ServerPlayerEntity;
//#else
import net.minecraft.entity.player.PlayerEntity;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC>=12105
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    @Unique
    private static final Translator translator = new Translator("rule.phantomSpawnAlert");

    @Inject(
        method = "spawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V",
            shift = At.Shift.AFTER
        )
    )
    private void broadcastMessage(
        ServerWorld world, boolean spawnMonsters, boolean spawnAnimals,
        //#if MC>=12105
        //$$ CallbackInfo ci,
        //#else
        CallbackInfoReturnable<Integer> cir,
        //#endif
        //#if MC>=12006
        //$$ @Local ServerPlayerEntity playerEntity,
        //#else
        @Local PlayerEntity playerEntity,
        //#endif
        @Local PhantomEntity phantom
    ) {
        if (AmsServerSettings.phantomSpawnAlert && phantom != null) {
            MinecraftServer server = world.getServer();
            String playerName = playerEntity.getGameProfile().getName();
            double x = phantom.getPos().getX();
            double y = phantom.getPos().getY();
            double z = phantom.getPos().getZ();
            String coord = x + ", " + y + ", " + z;
            Messenger.sendServerMessage(server, Messenger.s(translator.tr("msg", playerName, coord).getString()));
        }
    }
}