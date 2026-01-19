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

package club.mcams.carpet.network.payloads.handshake;

import club.mcams.carpet.AmsClient;
import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;

import net.minecraft.network.PacketByteBuf;

import java.util.HashSet;
import java.util.Set;

public class HandShakeS2CPayload extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.HANDSHAKE_S2C.getId();
    private final String modVersion;
    private final boolean isSupportServer;
    private final Set<String> supportedPackets;

    public HandShakeS2CPayload(String modVersion, boolean isSupportServer, Set<String> supportedPackets) {
        super(ID);
        this.modVersion = modVersion;
        this.isSupportServer = isSupportServer;
        this.supportedPackets = supportedPackets;
    }

    public HandShakeS2CPayload(PacketByteBuf buf) {
        super(ID);
        this.modVersion = NetworkUtil.readBufString(buf);
        this.isSupportServer = buf.readBoolean();
        int packetCount = buf.readVarInt();
        this.supportedPackets = new HashSet<>();

        for (int i = 0; i < packetCount; i++) {
            this.supportedPackets.add(NetworkUtil.readBufString(buf));
        }
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeString(this.modVersion);
        buf.writeBoolean(this.isSupportServer);
        buf.writeVarInt(this.supportedPackets.size());

        for (String packetId : this.supportedPackets) {
            buf.writeString(packetId);
        }
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnClientThread(() -> {
            if (this.modVersion.equals(AmsClient.getVersion())) {
                AmsClient.LOGGER.info("You joined server with matched carpet-ams-addition v{}", this.modVersion);
            } else {
                AmsClient.LOGGER.info("You joined server with mismatched carpet-ams-addition version (client: v{}, server: v{})", AmsClient.getVersion(), this.modVersion);
            }

            NetworkUtil.setServerSupport(this.isSupportServer);
            NetworkUtil.setServerSupportedPackets(this.supportedPackets);
        });
    }

    public static HandShakeS2CPayload create(String modVersion, boolean isSupportServer) {
        Set<String> supportedPackets = NetworkUtil.getLocalSupportedPackets();
        return new HandShakeS2CPayload(modVersion, isSupportServer, supportedPackets);
    }
}
