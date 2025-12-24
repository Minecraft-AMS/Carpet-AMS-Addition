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

package carpetamsaddition.network.payloads.rule.commandGetClientPlayerFPS;

import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.utils.MinecraftClientUtil;
import carpetamsaddition.utils.NetworkUtil;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class ClientPlayerFpsPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.CLIENT_PLAYER_FPS_S2C.getId();
    private final UUID uuid;

    private ClientPlayerFpsPayload_S2C(UUID uuid) {
        super(ID);
        this.uuid = uuid;
    }

    private ClientPlayerFpsPayload_S2C(FriendlyByteBuf buf) {
        super(ID);
        this.uuid = buf.readUUID();
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnClientThread(
            () -> NetworkUtil.sendC2SPacket(
                MinecraftClientUtil.getCurrentPlayer(),
                ClientPlayerFpsPayload_C2S.create(this.uuid, MinecraftClientUtil.getClientFps()),
                NetworkUtil.SendMode.NEED_SUPPORT
            )
        );
    }

    public static ClientPlayerFpsPayload_S2C create(UUID targetPlayerUuid) {
        return new ClientPlayerFpsPayload_S2C(targetPlayerUuid);
    }

    public static void register() {
        AMS_PayloadManager.register(ID, ClientPlayerFpsPayload_S2C::new);
    }
}
