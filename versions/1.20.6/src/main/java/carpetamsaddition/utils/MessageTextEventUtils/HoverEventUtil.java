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

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.item.ItemStack;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft <= 1.20.6")
@SuppressWarnings("unused")
public class HoverEventUtil {
    public static final HoverEvent.Action<Component> SHOW_TEXT = HoverEvent.Action.SHOW_TEXT;
    public static final HoverEvent.Action<HoverEvent.ItemStackInfo> SHOW_ITEM = HoverEvent.Action.SHOW_ITEM;
    public static final HoverEvent.Action<HoverEvent.EntityTooltipInfo> SHOW_ENTITY = HoverEvent.Action.SHOW_ENTITY;

    public static HoverEvent event(HoverEvent.Action<?> action, Object value) {
        if (action == SHOW_TEXT && value instanceof Component component) {
            return createEvent(SHOW_TEXT, component);
        }

        if (action == SHOW_ITEM && value instanceof ItemStack itemStack) {
            return createEvent(SHOW_ITEM, new HoverEvent.ItemStackInfo(itemStack));
        }

        if (action == SHOW_ENTITY && value instanceof HoverEvent.EntityTooltipInfo entityInfo) {
            return createEvent(SHOW_ENTITY, entityInfo);
        }

        throw new IllegalArgumentException("Invalid action or value type");
    }

    private static <T> HoverEvent createEvent(HoverEvent.Action<T> action, T value) {
        return new HoverEvent(action, value);
    }
}
