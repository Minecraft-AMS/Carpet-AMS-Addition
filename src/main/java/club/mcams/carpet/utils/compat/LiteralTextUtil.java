/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
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

package club.mcams.carpet.utils.compat;

import net.minecraft.text.*;

public class LiteralTextUtil {
    //#if MC<11900
    public static LiteralText compatText(String text) {
        return new LiteralText(text);
    }
    //#endif

    //#if MC>=11900
    //$$ public static MutableText compatText(String text) {
    //$$     return Text.literal(text);
    //$$ }
    //#endif

    public static Text createColoredText(String text, int rgb) {
        return compatText(text).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)));
    }

    public static Text createColoredText(String text, int rgb, boolean isItalic) {
        return compatText(text).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)).withItalic(isItalic));
    }

    public static Text createColoredText(String text, int rgb, boolean isBold, boolean isItalic) {
        return compatText(text).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)).withBold(isBold).withItalic(isItalic));
    }
}
