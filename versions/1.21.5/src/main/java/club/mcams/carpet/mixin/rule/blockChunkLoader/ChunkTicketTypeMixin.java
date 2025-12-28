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

package club.mcams.carpet.mixin.rule.blockChunkLoader;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper;

import net.minecraft.server.world.ChunkTicketType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import static club.mcams.carpet.helpers.rule.blockChunkLoader.BlockChunkLoaderHelper.registerTicketType;

@GameVersion(version = "Minecraft >= 1.21.5")
@Mixin(ChunkTicketType.class)
public abstract class ChunkTicketTypeMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerAmsTicketType(CallbackInfo ci) {
        final String TICKET_NAMESPACE = AmsServer.compactName;

        BlockChunkLoaderHelper.NOTE_BLOCK_TICKET_TYPE = registerTicketType(
            String.format("%s:note_block_loader", TICKET_NAMESPACE),
            BlockChunkLoaderHelper.getLoadTime(),
            //#if MC>=12109
            //$$ 15
            //#else
            true, ChunkTicketType.Use.LOADING_AND_SIMULATION
            //#endif
        );

        BlockChunkLoaderHelper.PISTON_BLOCK_TICKET_TYPE = registerTicketType(
            String.format("%s:piston_block_loader", TICKET_NAMESPACE),
            BlockChunkLoaderHelper.getLoadTime(),
            //#if MC>=12109
            //$$ 15
            //#else
            true, ChunkTicketType.Use.LOADING_AND_SIMULATION
            //#endif
        );

        BlockChunkLoaderHelper.BELL_BLOCK_TICKET_TYPE = registerTicketType(
            String.format("%s:bell_block_loader", TICKET_NAMESPACE),
            BlockChunkLoaderHelper.getLoadTime(),
            //#if MC>=12109
            //$$ 15
            //#else
            true, ChunkTicketType.Use.LOADING_AND_SIMULATION
            //#endif
        );
    }
}
