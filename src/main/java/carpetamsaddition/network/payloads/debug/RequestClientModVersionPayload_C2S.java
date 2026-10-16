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

package carpetamsaddition.network.payloads.debug;

import carpetamsaddition.commands.debug.network.AmspCommand;
import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.utils.NetworkUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class RequestClientModVersionPayload_C2S extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.REQUEST_CLIENT_MOD_VERSION_C2S.getId();

    private final String version;

    public RequestClientModVersionPayload_C2S(String version) {
        super(ID);
        this.version = version;
    }

    public RequestClientModVersionPayload_C2S(FriendlyByteBuf buf) {
        super(ID);
        this.version = buf.readUtf();
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeUtf(this.version);
    }

    public void handle(ServerPlayer sender) {
        NetworkUtil.executeOnServerThread(sender, () -> AmspCommand.receiveClientModVersion(sender, this.version));
    }

    public static RequestClientModVersionPayload_C2S create(String version) {
        return new RequestClientModVersionPayload_C2S(version);
    }
}
