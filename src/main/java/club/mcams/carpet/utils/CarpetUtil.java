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

package club.mcams.carpet.utils;

import carpet.settings.ParsedRule;

import org.jetbrains.annotations.NotNull;

public class CarpetUtil {
    public static String getRuleName(@NotNull ParsedRule<?> rule) {
        //#if MC>=11900
        //$$ return rule.name();
        //#else
        return rule.name;
        //#endif
    }

    public static String getRuleDefaultValue(@NotNull ParsedRule<?> rule) {
        //#if MC>=11900
        //$$ return String.valueOf(rule.defaultValue());
        //#else
        return rule.defaultAsString;
        //#endif
    }

    public static String getRuleCurrentValue(@NotNull ParsedRule<?> rule) {
        //#if MC>=11900
        //$$ return String.valueOf(rule.value());
        //#else
        return rule.getAsString();
        //#endif
    }
}
