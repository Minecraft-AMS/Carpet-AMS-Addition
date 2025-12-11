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

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerMod;
import club.mcams.carpet.utils.PlayerUtil;
import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class HandShakeC2SPayload extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.HANDSHAKE_C2S.getId();
    private final String modVersion;
    private final UUID playerUuid;

    private HandShakeC2SPayload(String modVersion, UUID playerUuid) {
        super(ID);
        this.modVersion = modVersion;
        this.playerUuid = playerUuid;
    }

    private HandShakeC2SPayload(PacketByteBuf buf) {
        super(ID);
        this.modVersion = buf.readString();
        this.playerUuid = buf.readUuid();
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeString(this.modVersion);
        buf.writeUuid(this.playerUuid);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnServerThread(() -> {
            ServerPlayerEntity player = PlayerUtil.getServerPlayerEntity(this.playerUuid);
            String playerName = player != null ? PlayerUtil.getName(player) : "Unknown Player";

            if (this.modVersion.equals(AmsServerMod.getVersion())) {
                AmsServer.LOGGER.info("{} joined with matched carpet-ams-addition v{}", playerName, this.modVersion);
            } else {
                AmsServer.LOGGER.info("{} joined with mismatched carpet-ams-addition version (client: v{}, server: v{})", playerName, this.modVersion, AmsServerMod.getVersion());
            }

            NetworkUtil.addSupportClient(this.playerUuid);

            if (player != null) {
                AmsServer.getInstance().sendS2CPacketOnHandShake(player);
            } else {
                AmsServer.LOGGER.warn("Could not find player entity for UUID: {}", this.playerUuid);
            }
        });
    }

    public static HandShakeC2SPayload create(String modVersion, UUID playerUuid) {
        return new HandShakeC2SPayload(modVersion, playerUuid);
    }

    public static void register() {
        AMS_PayloadManager.register(ID, HandShakeC2SPayload::new);
    }
}
