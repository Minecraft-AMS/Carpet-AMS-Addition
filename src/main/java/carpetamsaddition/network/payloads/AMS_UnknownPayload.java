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

package carpetamsaddition.network.payloads;

import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.utils.Noop;

import net.minecraft.network.FriendlyByteBuf;

import org.apache.logging.log4j.LogManager;

public class AMS_UnknownPayload extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.UNKNOWN.getId();

    private AMS_UnknownPayload() {
        super(ID);
    }

    private AMS_UnknownPayload(FriendlyByteBuf buf) {
        super(ID);
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        Noop.noop();
    }

    @Override
    public void handle() {
        LogManager.getLogger().warn("Received unknown custom payload from carpet ams addition.");
    }

    public static AMS_UnknownPayload create() {
        return new AMS_UnknownPayload();
    }

    public static void register() {
        AMS_PayloadManager.register(ID, AMS_UnknownPayload::new);
    }
}
