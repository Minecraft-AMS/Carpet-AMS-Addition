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

package club.mcams.carpet.network;

import club.mcams.carpet.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import club.mcams.carpet.network.rule.commandCustomBlockHardness.CustomBlockHardnessS2CPacket;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.block.BlockState;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(CustomBlockHardnessS2CPacket.ID, (client, handler, buf, responseSender) -> {
            Map<BlockState, Float> map = new HashMap<>();
            CustomBlockHardnessS2CPacket.decode(buf, map);
            CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.clear();
            CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP.putAll(map);
        });
    }
}
