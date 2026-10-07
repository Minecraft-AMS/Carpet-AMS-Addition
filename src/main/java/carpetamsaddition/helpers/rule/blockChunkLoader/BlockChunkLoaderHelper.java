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

package carpetamsaddition.helpers.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.mixin.rule.blockChunkLoader.TicketTypeMixin;
import carpetamsaddition.utils.compat.ChunkTicketTypeWrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class BlockChunkLoaderHelper {
    /** 实际时长由 {@link TicketTypeMixin} 动态提供。 */
    private static final int REGISTRATION_TIMEOUT_TICKS = 300;

    public static ChunkTicketTypeWrapper NOTE_BLOCK_TICKET_TYPE;
    public static ChunkTicketTypeWrapper PISTON_BLOCK_TICKET_TYPE;
    public static ChunkTicketTypeWrapper BELL_BLOCK_TICKET_TYPE;

    public static ChunkTicketTypeWrapper registerTicketType(String id, int flags) {
        ChunkTicketTypeWrapper ticketType = ChunkTicketTypeWrapper.register(id, REGISTRATION_TIMEOUT_TICKS, flags);
        ((BlockLoaderTicketTypeAccess) ticketType.getNativeType()).ams$enableBlockLoaderTimeout();
        return ticketType;
    }

    public static void addNoteBlockTicket(ServerLevel world, BlockPos blockPos) {
        NOTE_BLOCK_TICKET_TYPE.addTicket(world, blockPos, CarpetAMSAdditionSettings.blockChunkLoaderRangeController);
    }

    public static void addPistonBlockTicket(ServerLevel world, BlockPos blockPos) {
        PISTON_BLOCK_TICKET_TYPE.addTicket(world, blockPos, CarpetAMSAdditionSettings.blockChunkLoaderRangeController);
    }

    public static void addBellBlockTicket(ServerLevel world, BlockPos blockPos) {
        BELL_BLOCK_TICKET_TYPE.addTicket(world, blockPos, CarpetAMSAdditionSettings.blockChunkLoaderRangeController);
    }
}
