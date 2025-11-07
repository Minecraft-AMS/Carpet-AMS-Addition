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

import club.mcams.carpet.commands.rule.commandGetClientPlayerFps.GetClientPlayerFpsRegistry;
import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.utils.NetworkUtil;

import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class ClientPlayerFpsPayload_C2S extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.CLIENT_PLAYER_FPS_C2S.getId();
    private final UUID playerUuid;
    private final int fps;

    private ClientPlayerFpsPayload_C2S(UUID playerUuid, int fps) {
        super(ID);
        this.playerUuid = playerUuid;
        this.fps = fps;
    }

    private ClientPlayerFpsPayload_C2S(PacketByteBuf buf) {
        super(ID);
        this.playerUuid = buf.readUuid();
        this.fps = buf.readInt();
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeInt(fps);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnServerThread(() -> GetClientPlayerFpsRegistry.sendFpsResult(this.playerUuid, this.fps));
    }

    public static ClientPlayerFpsPayload_C2S create(UUID playerUuid, int fps) {
        return new ClientPlayerFpsPayload_C2S(playerUuid, fps);
    }

    public static void register() {
        AMS_PayloadManager.register(ID, ClientPlayerFpsPayload_C2S::new);
    }
}
