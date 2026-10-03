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

package carpetamsaddition.utils.messageTextEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
//#if MC>=260000
//$$ import net.minecraft.world.item.ItemStackTemplate;
//#else
import net.minecraft.world.item.ItemStack;
//#endif

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unused")
public class HoverEventUtil {
    //#if MC>12006
    public static final HoverEvent.Action SHOW_TEXT = HoverEvent.Action.SHOW_TEXT;
    public static final HoverEvent.Action SHOW_ITEM = HoverEvent.Action.SHOW_ITEM;
    public static final HoverEvent.Action SHOW_ENTITY = HoverEvent.Action.SHOW_ENTITY;
    private static final Map<HoverEvent.Action, Function<Object, HoverEvent>> HOVER_EVENT_ACTION_MAP = new HashMap<>();
    //#else
    //$$ public static final HoverEvent.Action<Component> SHOW_TEXT = HoverEvent.Action.SHOW_TEXT;
    //$$ public static final HoverEvent.Action<HoverEvent.ItemStackInfo> SHOW_ITEM = HoverEvent.Action.SHOW_ITEM;
    //$$ public static final HoverEvent.Action<HoverEvent.EntityTooltipInfo> SHOW_ENTITY = HoverEvent.Action.SHOW_ENTITY;
    //#endif

    public static HoverEvent event(HoverEvent.Action action, Object value) {
        //#if MC>12006
        return Optional.ofNullable(HOVER_EVENT_ACTION_MAP.get(action)).map(function -> function.apply(value)).orElseThrow(() -> new IllegalArgumentException("Invalid action or value type"));
        //#else
        //$$ if (action == SHOW_TEXT && value instanceof Component) {
        //$$     return createEvent(SHOW_TEXT, (Component) value);
        //$$ }
        //$$
        //$$ if (action == SHOW_ITEM && value instanceof ItemStack) {
        //$$     return createEvent(SHOW_ITEM, new HoverEvent.ItemStackInfo((ItemStack) value));
        //$$ }
        //$$
        //$$ if (action == SHOW_ENTITY && value instanceof HoverEvent.EntityTooltipInfo) {
        //$$     return createEvent(SHOW_ENTITY, (HoverEvent.EntityTooltipInfo) value);
        //$$ }
        //$$
        //$$ throw new IllegalArgumentException("Invalid action or value type");
        //#endif
    }

    //#if MC>12006
    static {
        HOVER_EVENT_ACTION_MAP.put(SHOW_TEXT, value -> new HoverEvent.ShowText((Component) value));
        HOVER_EVENT_ACTION_MAP.put(
            SHOW_ITEM, value ->
            //#if MC>=260000
            //$$ new HoverEvent.ShowItem((ItemStackTemplate) value)
            //#else
            new HoverEvent.ShowItem((ItemStack) value)
            //#endif
        );
        HOVER_EVENT_ACTION_MAP.put(SHOW_ENTITY, value -> new HoverEvent.ShowEntity((HoverEvent.EntityTooltipInfo) value));
    }
    //#else
    //$$ private static <T> HoverEvent createEvent(HoverEvent.Action<T> action, T value) {
    //$$     return new HoverEvent(action, value);
    //$$ }
    //#endif
}
