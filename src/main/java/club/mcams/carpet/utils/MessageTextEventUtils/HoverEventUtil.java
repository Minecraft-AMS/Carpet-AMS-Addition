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

package club.mcams.carpet.utils.MessageTextEventUtils;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 1.21.5")
@SuppressWarnings("unused")
public class HoverEventUtil {
    public static HoverEvent.Action<Text> SHOW_TEXT = HoverEvent.Action.SHOW_TEXT;
    public static HoverEvent.Action<HoverEvent.ItemStackContent> SHOW_ITEM = HoverEvent.Action.SHOW_ITEM;
    public static HoverEvent.Action<HoverEvent.EntityContent> SHOW_ENTITY = HoverEvent.Action.SHOW_ENTITY;

    public static <T> HoverEvent event(HoverEvent.Action<T> action, T value) {
        return new HoverEvent(action, value);
    }
}
