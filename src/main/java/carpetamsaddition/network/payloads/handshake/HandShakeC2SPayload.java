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

package carpetamsaddition.network.payloads.handshake;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.CarpetAMSAdditionMod;
import carpetamsaddition.utils.PlayerUtil;
import carpetamsaddition.utils.NetworkUtil;
import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HandShakeC2SPayload extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.HANDSHAKE_C2S.getId();
    private final String modVersion;
    private final UUID playerUuid;
    private final Set<String> supportedPackets;

    public HandShakeC2SPayload(String modVersion, UUID playerUuid, Set<String> supportedPackets) {
        super(ID);
        this.modVersion = modVersion;
        this.playerUuid = playerUuid;
        this.supportedPackets = supportedPackets;
    }

    public HandShakeC2SPayload(FriendlyByteBuf buf) {
        super(ID);

        this.modVersion = buf.readUtf();
        this.playerUuid = buf.readUUID();
        int packetCount = buf.readVarInt();
        this.supportedPackets = new HashSet<>();

        for (int i = 0; i < packetCount; i++) {
            this.supportedPackets.add(buf.readUtf());
        }
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeUtf(this.modVersion);
        buf.writeUUID(this.playerUuid);
        buf.writeVarInt(this.supportedPackets.size());

        for (String packetId : this.supportedPackets) {
            buf.writeUtf(packetId);
        }
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnServerThread(() -> {
            ServerPlayer player = PlayerUtil.getServerPlayerEntity(this.playerUuid);
            String playerName = player != null ? PlayerUtil.getName(player) : "Unknown Player";

            if (this.modVersion.equals(CarpetAMSAdditionMod.getVersion())) {
                CarpetAMSAdditionServer.LOGGER.info("{} joined with matched carpet-ams-addition v{}", playerName, this.modVersion);
            } else {
                CarpetAMSAdditionServer.LOGGER.info("{} joined with mismatched carpet-ams-addition version (client: v{}, server: v{})", playerName, this.modVersion, CarpetAMSAdditionMod.getVersion());
            }

            NetworkUtil.setClientSupportedPackets(this.playerUuid, this.supportedPackets);
            NetworkUtil.addSupportClient(this.playerUuid);

            if (player != null) {
                CarpetAMSAdditionServer.getInstance().sendS2CPacketOnHandShake(player);
            } else {
                CarpetAMSAdditionServer.LOGGER.warn("Could not find player entity for UUID: {}", this.playerUuid);
            }
        });
    }

    public static HandShakeC2SPayload create(String modVersion, UUID playerUuid) {
        Set<String> supportedPackets = NetworkUtil.getLocalSupportedPackets();
        return new HandShakeC2SPayload(modVersion, playerUuid, supportedPackets);
    }
}
