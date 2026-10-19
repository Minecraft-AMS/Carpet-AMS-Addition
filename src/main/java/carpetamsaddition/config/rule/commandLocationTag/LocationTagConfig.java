/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.config.rule.commandLocationTag;

import carpetamsaddition.helpers.rule.commandLocationTag.LocationTagIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LocationTagConfig {
    public int version = 1;
    public String marker = "amsloc";
    public String sign = "minecraft:oak_sign";
    public int nextId = 1;
    public List<LocationTagIndex.Entry> entries = new ArrayList<>();

    public static boolean validMarker(String marker) {
        return marker != null && !marker.trim().isEmpty() && marker.indexOf('\n') < 0 && marker.indexOf('\r') < 0;
    }

    public void validate() throws IOException {
        if (version != 1 || !validMarker(marker) || sign == null || sign.isEmpty() || entries == null) {
            throw new IOException("Invalid location tag configuration");
        }
    }
}
