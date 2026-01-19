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

package club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS;

import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.utils.MinecraftClientUtil;
import club.mcams.carpet.utils.NetworkUtil;

import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class ClientPlayerFpsPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.CLIENT_PLAYER_FPS_S2C.getId();
    private final UUID uuid;

    public ClientPlayerFpsPayload_S2C(UUID uuid) {
        super(ID);
        this.uuid = uuid;
    }

    public ClientPlayerFpsPayload_S2C(PacketByteBuf buf) {
        super(ID);
        this.uuid = buf.readUuid();
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeUuid(this.uuid);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnClientThread(() ->
            NetworkUtil.sendC2SPacket(MinecraftClientUtil.getCurrentPlayer(), ClientPlayerFpsPayload_C2S.create(this.uuid, MinecraftClientUtil.getClientFps()), NetworkUtil.SendMode.NEED_SUPPORT)
        );
    }

    public static ClientPlayerFpsPayload_S2C create(UUID targetPlayerUuid) {
        return new ClientPlayerFpsPayload_S2C(targetPlayerUuid);
    }
}
