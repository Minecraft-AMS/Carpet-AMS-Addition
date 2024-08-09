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

package club.mcams.carpet.mixin.rule.commandPlayerChunkLoadController;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.commandPlayerChunkLoadController.ChunkLoading;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkTicketManager.class)
public class ChunkTicketManagerMixin {
    //#if MC>=11800
    @WrapOperation(
            method = "handleChunkEnter(Lnet/minecraft/util/math/ChunkSectionPos;Lnet/minecraft/server/network/ServerPlayerEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ChunkTicketManager;getPlayerSimulationLevel()I"
            )
    )
    private int tweakPlayerSimulationLevel(ChunkTicketManager instance, Operation<Integer> original, @Local(argsOnly = true) ServerPlayerEntity player) {
        int level = original.call(instance);
        if (AmsServerSettings.commandPlayerChunkLoadController && ChunkLoading.isPlayerUnLoading(player)) {
            level = Math.max(level, ChunkLoading.getSimulationLevel(player));
        }
        return level;
    }
    //#endif
}
