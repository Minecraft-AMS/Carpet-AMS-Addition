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

package carpetamsaddition.mixin.rule.phantomSpawnAlert;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.messenger.Messenger;
import carpetamsaddition.utils.PlayerUtil;
import carpetamsaddition.utils.Layout;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.MutableComponent;
//#if MC>=12006
import net.minecraft.server.level.ServerPlayer;
//#else
//$$ import net.minecraft.world.entity.player.Player;
//#endif
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin {
    @Unique
    private static final Translator tr = new Translator("rule.phantomSpawnAlert");

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"
        )
    )
    private void broadcastMessage(
        ServerLevel world, Entity entity, Operation<Void> original,
        //#if MC>=12006
        @Local ServerPlayer playerEntity
        //#else
        //$$ @Local Player playerEntity
        //#endif
    ) {
        original.call(world, entity);

        if (CarpetAMSAdditionSettings.phantomSpawnAlert && world.getEntity(entity.getId()) == entity) {
            MutableComponent coordinateText = Messenger.c(
                Messenger.f(Messenger.s("@ "), Layout.YELLOW),
                Messenger.f(Messenger.s("[ " + Messenger.formatPos(entity.getOnPos()) + " ]"), Layout.GREEN)
            );
            Messenger.sendServerMessage(world.getServer(), Messenger.c(
                Messenger.f(Messenger.s("<phantomSpawnAlert> "), Layout.AQUA),
                Messenger.f(Messenger.s(PlayerUtil.getName(playerEntity)), Layout.YELLOW),
                Messenger.f(tr.tr("msg", coordinateText), Layout.LIGHT_PURPLE)
            ));
        }
    }
}
