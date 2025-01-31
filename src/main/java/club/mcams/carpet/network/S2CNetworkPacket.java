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

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@SuppressWarnings("unused")
@GameVersion(version = "Only Minecraft >= 1.20.5")
public class S2CNetworkPacket {
    public static void register() {
        /*
            只有 Minecraft >= 1.20.5 需要这个
            例：PayloadTypeRegistry.playS2C().register(CustomBlockHardnessS2CPacket.ID, CustomBlockHardnessS2CPacket.CODEC);
         */
    }
}
