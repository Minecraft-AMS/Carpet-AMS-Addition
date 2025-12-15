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

package carpetamsaddition.debug;

import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.MinecraftServerUtil;

import org.jetbrains.annotations.TestOnly;

@SuppressWarnings("unused")
public class DebugTools {
    @TestOnly
    public static void printToChatBar(Object content) {
        if (MinecraftServerUtil.serverIsRunning()) {
            Messenger.sendServerMessage(MinecraftServerUtil.getServer(), Messenger.c(String.format("y <AMS Debug> %s", content.toString())));
        }
    }
}
