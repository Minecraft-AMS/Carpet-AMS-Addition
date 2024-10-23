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

package club.mcams.carpet.mixin.rule.fakePlayerDefaultSurvivalMode;

import carpet.commands.PlayerCommand;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerCommand.class)
public abstract class Carpet_PlayerCommandMixin {
    @WrapOperation(
        method = "spawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;getGameMode()Lnet/minecraft/world/GameMode;"
        )
    )
    private static GameMode fakePlayerDefaultSurvivalMode(ServerPlayerInteractionManager managerInstance, Operation<GameMode> original) {
        return AmsServerSettings.fakePlayerDefaultSurvivalMode ? GameMode.SURVIVAL : original.call(managerInstance);
    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    @ModifyExpressionValue(
        method = "spawn",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z"
        )
    )
    private static boolean noFly(boolean original) {
        return AmsServerSettings.fakePlayerDefaultSurvivalMode ? false : original;
    }
}
