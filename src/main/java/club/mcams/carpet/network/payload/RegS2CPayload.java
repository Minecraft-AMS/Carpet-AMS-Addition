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

package club.mcams.carpet.network.payload;

import club.mcams.carpet.network.payload.handshake.HandShakeS2CPayload;
import club.mcams.carpet.network.payload.rule.commandCustomBlockHardness.CustomBlockHardnessPayload;

public final class RegS2CPayload {
    private RegS2CPayload() {}

    public static void register() {
        HandShakeS2CPayload.register();
        CustomBlockHardnessPayload.register();
        AMS_UnknownPayload.register();
    }
}
