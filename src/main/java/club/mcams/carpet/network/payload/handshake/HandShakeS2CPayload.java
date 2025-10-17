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

package club.mcams.carpet.network.payload.handshake;

import club.mcams.carpet.AmsClient;
import club.mcams.carpet.network.PacketId;
import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.network.payload.AMS_CustomPayload;

import net.minecraft.network.PacketByteBuf;

public class HandShakeS2CPayload extends AMS_CustomPayload {
    private static final String ID = PacketId.HANDSHAKE_S2C.getId();
    private final String modVersion;
    private final boolean isSupportServer;

    private HandShakeS2CPayload(String modVersion, boolean isSupportServer) {
        super(ID);
        this.modVersion = modVersion;
        this.isSupportServer = isSupportServer;
    }

    private HandShakeS2CPayload(PacketByteBuf buf) {
        super(ID);
        this.modVersion = readString(buf);
        this.isSupportServer = buf.readBoolean();
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeString(this.modVersion);
        buf.writeBoolean(this.isSupportServer);
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
        });
    }

    public static HandShakeS2CPayload create(String modVersion, boolean isSupportServer) {
        return new HandShakeS2CPayload(modVersion, isSupportServer);
    }

    public static void register() {
        AMS_CustomPayload.register(ID, HandShakeS2CPayload::new);
    }
}
