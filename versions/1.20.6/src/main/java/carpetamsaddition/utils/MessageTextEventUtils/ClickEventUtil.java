/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.utils.MessageTextEventUtils;

import net.minecraft.network.chat.ClickEvent;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft <= 1.20.6")
@SuppressWarnings("unused")
public class ClickEventUtil {
    public static final ClickEvent.Action OPEN_URL = ClickEvent.Action.OPEN_URL;
    public static final ClickEvent.Action OPEN_FILE = ClickEvent.Action.OPEN_FILE;
    public static final ClickEvent.Action RUN_COMMAND = ClickEvent.Action.RUN_COMMAND;
    public static final ClickEvent.Action SUGGEST_COMMAND = ClickEvent.Action.SUGGEST_COMMAND;
    public static final ClickEvent.Action CHANGE_PAGE = ClickEvent.Action.CHANGE_PAGE;
    public static final ClickEvent.Action COPY_TO_CLIPBOARD = ClickEvent.Action.COPY_TO_CLIPBOARD;

    public static ClickEvent event(ClickEvent.Action action, Object value) {
        if (action == OPEN_URL && value instanceof java.net.URI uri) {
            return new ClickEvent(action, uri.toString());
        }

        if (value instanceof String string) {
            return new ClickEvent(action, string);
        }

        throw new IllegalArgumentException("Expected a String value for " + action + " action");
    }
}
