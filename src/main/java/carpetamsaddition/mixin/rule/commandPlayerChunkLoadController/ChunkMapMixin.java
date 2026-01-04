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

package carpetamsaddition.mixin.rule.commandPlayerChunkLoadController;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.commandPlayerChunkLoadController.ChunkLoading;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ChunkMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
    @Inject(method = "skipPlayer", at = @At("HEAD"), cancellable = true)
    private void doesNotGenerateChunks(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(CarpetAMSAdditionSettings.commandPlayerChunkLoadController, "false")) {
            String playerName = player.getName().getString();
            if (!ChunkLoading.onlinePlayerMap.getOrDefault(playerName, true)) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }
}