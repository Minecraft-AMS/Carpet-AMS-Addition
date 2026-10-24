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
import java.util.Objects;

public class HandShakeC2SPayload extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.HANDSHAKE_C2S.getId();
    private final String modVersion;
    private final Set<String> supportedPackets;

    public HandShakeC2SPayload(String modVersion, Set<String> supportedPackets) {
        super(ID);
        this.modVersion = modVersion;
        this.supportedPackets = supportedPackets;
    }

    public HandShakeC2SPayload(FriendlyByteBuf buf) {
        super(ID);

        this.modVersion = buf.readUtf();
        int packetCount = buf.readVarInt();
        this.supportedPackets = new HashSet<>();

        for (int i = 0; i < packetCount; i++) {
            this.supportedPackets.add(buf.readUtf());
        }
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeUtf(this.modVersion);
        buf.writeVarInt(this.supportedPackets.size());

        for (String packetId : this.supportedPackets) {
            buf.writeUtf(packetId);
        }
    }

    @Override
    public void handle(ServerPlayer sender) {
        String playerName = PlayerUtil.getName(sender);

        if (Objects.equals(this.modVersion, CarpetAMSAdditionMod.getVersion())) {
            CarpetAMSAdditionServer.LOGGER.info("{} joined with matched carpet-ams-addition v{}", playerName, this.modVersion);
        } else {
            CarpetAMSAdditionServer.LOGGER.info("{} joined with mismatched carpet-ams-addition version (client: v{}, server: v{})", playerName, this.modVersion, CarpetAMSAdditionMod.getVersion());
        }

        NetworkUtil.setClientSupportedPackets(sender, this.supportedPackets);
        NetworkUtil.addSupportClient(sender);

        CarpetAMSAdditionServer.getInstance().sendS2CPacketOnHandShake(sender);
    }

    public static HandShakeC2SPayload create(String modVersion) {
        Set<String> supportedPackets = NetworkUtil.getLocalSupportedPackets();
        return new HandShakeC2SPayload(modVersion, supportedPackets);
    }
}
