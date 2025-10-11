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

package club.mcams.carpet.utils;

import club.mcams.carpet.network.payload.AMS_CustomPayload;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class NetworkUtil {
    public static void broadcastDataPack(MinecraftServer server, AMS_CustomPayload payload) {
        server.getPlayerManager().getPlayerList().forEach(player -> sendS2CPacket(player, payload));
    }

    public static void sendS2CPacket(ServerPlayerEntity player, AMS_CustomPayload payload) {
        payload.sendS2CPacket(player);
    }

    public static void sendC2SPacket(ClientPlayerEntity player, AMS_CustomPayload payload) {
        payload.sendC2SPacket(player);
    }
}
