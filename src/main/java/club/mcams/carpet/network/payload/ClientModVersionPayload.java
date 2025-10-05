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

package club.mcams.carpet.network.payload;

import club.mcams.carpet.AmsServer;

import net.minecraft.network.PacketByteBuf;

public class ClientModVersionPayload extends AMS_CustomPayload {
    private static final String PACKET_ID = "client_carpetamsaddition_version";

    private final String modVersion;
    private final String playerName;

    public ClientModVersionPayload(String modVersion, String playerName) {
        super(PACKET_ID);
        this.modVersion = modVersion;
        this.playerName = playerName;
    }

    public ClientModVersionPayload(PacketByteBuf buf) {
        super(PACKET_ID);
        this.modVersion = readString(buf);
        this.playerName = readString(buf);
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeString(this.modVersion);
        buf.writeString(this.playerName);
    }

    @Override
    public void handle() {
        AmsServer.LOGGER.info("{} joined with carpet-ams-addition v{}", this.playerName, this.modVersion);
    }

    public static ClientModVersionPayload create(String modVersion, String playerName) {
        return new ClientModVersionPayload(modVersion, playerName);
    }

    public static void register() {
        AMS_CustomPayload.register(PACKET_ID, ClientModVersionPayload::new);
    }
}
