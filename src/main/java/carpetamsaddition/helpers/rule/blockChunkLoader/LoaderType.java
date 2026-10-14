/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
 */

package carpetamsaddition.helpers.rule.blockChunkLoader;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.compat.ChunkTicketTypeWrapper;
import carpetamsaddition.config.rule.blockChunkLoader.BlockLoaderConfig;
import carpetamsaddition.utils.compat.TicketTypeWrapper;

public enum LoaderType {
    NOTEBLOCK("noteblock", "note_block_loader", TicketTypeWrapper.FLAG_LOADING | TicketTypeWrapper.FLAG_SIMULATION | TicketTypeWrapper.FLAG_KEEP_DIMENSION_ACTIVE),
    PISTON("piston", "piston_block_loader", TicketTypeWrapper.FLAG_LOADING | TicketTypeWrapper.FLAG_SIMULATION | TicketTypeWrapper.FLAG_KEEP_DIMENSION_ACTIVE),
    BELL("bell", "bell_block_loader", TicketTypeWrapper.FLAG_LOADING | TicketTypeWrapper.FLAG_SIMULATION | TicketTypeWrapper.FLAG_KEEP_DIMENSION_ACTIVE);

    public final String id;
    private final String ticketId;
    private final int flags;
    private ChunkTicketTypeWrapper ticketType;

    LoaderType(String id, String ticketId, int flags) {
        this.id = id;
        this.ticketId = ticketId;
        this.flags = flags;
    }

    public String permission() {
        switch (this) {
            case NOTEBLOCK: return CarpetAMSAdditionSettings.commandNoteblockChunkloader;
            case PISTON: return CarpetAMSAdditionSettings.commandPistonChunkloader;
            default: return CarpetAMSAdditionSettings.commandBellChunkloader;
        }
    }

    public void registerTicketType() {
        ticketType = ChunkTicketTypeWrapper.register(String.format("%s:%s", CarpetAMSAdditionServer.compactName, ticketId), BlockLoaderConfig.MAX_TIME, flags);
        ((BlockLoaderTicketTypeAccess) ticketType.getNativeType()).ams$setLoaderType(this);
    }

    public ChunkTicketTypeWrapper ticketType() {
        return ticketType;
    }
}
