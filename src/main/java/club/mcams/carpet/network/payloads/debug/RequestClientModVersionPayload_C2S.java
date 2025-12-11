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

package club.mcams.carpet.network.payloads.debug;

import club.mcams.carpet.commands.debug.network.AmspCommandRegistry;
import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.utils.NetworkUtil;

import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class RequestClientModVersionPayload_C2S extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.REQUEST_CLIENT_MOD_VERSION_C2S.getId();

    private final UUID uuid;
    private final String version;

    private RequestClientModVersionPayload_C2S(String version, UUID uuid) {
        super(ID);
        this.version = version;
        this.uuid = uuid;
    }

    public RequestClientModVersionPayload_C2S(PacketByteBuf buf) {
        super(ID);
        this.version = buf.readString();
        this.uuid = buf.readUuid();
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeString(this.version);
        buf.writeUuid(this.uuid);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnServerThread(() -> AmspCommandRegistry.clientModVersion.put(this.uuid, this.version));
    }

    public static RequestClientModVersionPayload_C2S create(String version, UUID uuid) {
        return new RequestClientModVersionPayload_C2S(version, uuid);
    }

    public static void register() {
        AMS_PayloadManager.register(ID, RequestClientModVersionPayload_C2S::new);
    }
}
