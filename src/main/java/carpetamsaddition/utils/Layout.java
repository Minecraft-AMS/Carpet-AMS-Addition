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

package carpetamsaddition.utils;

import net.minecraft.ChatFormatting;

public enum Layout {
    BLACK(ChatFormatting.BLACK),
    DARK_BLUE(ChatFormatting.DARK_BLUE),
    DARK_GREEN(ChatFormatting.DARK_GREEN),
    DARK_AQUA(ChatFormatting.DARK_AQUA),
    DARK_RED(ChatFormatting.DARK_RED),
    DARK_PURPLE(ChatFormatting.DARK_PURPLE),
    GOLD(ChatFormatting.GOLD),
    GRAY(ChatFormatting.GRAY),
    DARK_GRAY(ChatFormatting.DARK_GRAY),
    BLUE(ChatFormatting.BLUE),
    GREEN(ChatFormatting.GREEN),
    AQUA(ChatFormatting.AQUA),
    RED(ChatFormatting.RED),
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE),
    YELLOW(ChatFormatting.YELLOW),
    WHITE(ChatFormatting.WHITE),
    OBFUSCATED(ChatFormatting.OBFUSCATED),
    BOLD(ChatFormatting.BOLD),
    STRIKETHROUGH(ChatFormatting.STRIKETHROUGH),
    UNDERLINE(ChatFormatting.UNDERLINE),
    ITALIC(ChatFormatting.ITALIC),
    RESET(ChatFormatting.RESET);

    private final ChatFormatting formatting;

    Layout(ChatFormatting formatting) {
        this.formatting = formatting;
    }

    public ChatFormatting getFormatting() {
        return formatting;
    }

    @Override
    public String toString() {
        return formatting.toString();
    }
}

/*
    //////////////////////////////////Minecraft Style//////////////////////////////////
    BLACK("BLACK", '0', 0, 0),
    DARK_BLUE("DARK_BLUE", '1', 1, 170),
    DARK_GREEN("DARK_GREEN", '2', 2, 43520),
    DARK_AQUA("DARK_AQUA", '3', 3, 43690),
    DARK_RED("DARK_RED", '4', 4, 11141120),
    DARK_PURPLE("DARK_PURPLE", '5', 5, 11141290),
    GOLD("GOLD", '6', 6, 16755200),
    GRAY("GRAY", '7', 7, 11184810),
    DARK_GRAY("DARK_GRAY", '8', 8, 5592405),
    BLUE("BLUE", '9', 9, 5592575),
    GREEN("GREEN", 'a', 10, 5635925),
    AQUA("AQUA", 'b', 11, 5636095),
    RED("RED", 'c', 12, 16733525),
    LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, 16733695),
    YELLOW("YELLOW", 'e', 14, 16777045),
    WHITE("WHITE", 'f', 15, 16777215),
    OBFUSCATED("OBFUSCATED", 'k', true),
    BOLD("BOLD", 'l', true),
    STRIKETHROUGH("STRIKETHROUGH", 'm', true),
    UNDERLINE("UNDERLINE", 'n', true),
    ITALIC("ITALIC", 'o', true),
    RESET("RESET", 'r', -1, (Integer)null);

    "\u001B[0;30m", // Black §0
    "\u001B[0;34m", // Dark Blue §1
    "\u001B[0;32m", // Dark Green §2
    "\u001B[0;36m", // Dark Aqua §3
    "\u001B[0;31m", // Dark Red §4
    "\u001B[0;35m", // Dark Purple §5
    "\u001B[0;33m", // Gold §6
    "\u001B[0;37m", // Gray §7
    "\u001B[0;30;1m",  // Dark Gray §8
    "\u001B[0;34;1m",  // Blue §9
    "\u001B[0;32;1m",  // Green §a
    "\u001B[0;36;1m",  // Aqua §b
    "\u001B[0;31;1m",  // Red §c
    "\u001B[0;35;1m",  // Light Purple §d
    "\u001B[0;33;1m",  // Yellow §e
    "\u001B[0;37;1m",  // White §f
    "\u001B[5m",       // Obfuscated §k
    "\u001B[21m",      // Bold §l
    "\u001B[9m",       // Strikethrough §m
    "\u001B[4m",       // Underline §n
    "\u001B[3m",       // Italic §o
    ANSI_RESET,        // Reset §r
*/

/*
    //////////////////////////////////Carpet Style//////////////////////////////////
    ITALIC      ('i', (s, f) -> s.withItalic(true)),
    STRIKE      ('s', (s, f) -> s.withFormatting(Formatting.STRIKETHROUGH)),
    UNDERLINE   ('u', (s, f) -> s.withFormatting(Formatting.UNDERLINE)),
    BOLD        ('b', (s, f) -> s.withBold(true)),
    OBFUSCATE   ('o', (s, f) -> s.withFormatting(Formatting.OBFUSCATED)),

    WHITE       ('w', (s, f) -> s.withColor(Formatting.WHITE)),
    YELLOW      ('y', (s, f) -> s.withColor(Formatting.YELLOW)),
    LIGHT_PURPLE('m', (s, f) -> s.withColor(Formatting.LIGHT_PURPLE)), // magenta
    RED         ('r', (s, f) -> s.withColor(Formatting.RED)),
    AQUA        ('c', (s, f) -> s.withColor(Formatting.AQUA)), // cyan
    GREEN       ('l', (s, f) -> s.withColor(Formatting.GREEN)), // lime
    BLUE        ('t', (s, f) -> s.withColor(Formatting.BLUE)), // light blue, teal
    DARK_GRAY   ('f', (s, f) -> s.withColor(Formatting.DARK_GRAY)),
    GRAY        ('g', (s, f) -> s.withColor(Formatting.GRAY)),
    GOLD        ('d', (s, f) -> s.withColor(Formatting.GOLD)),
    DARK_PURPLE ('p', (s, f) -> s.withColor(Formatting.DARK_PURPLE)), // purple
    DARK_RED    ('n', (s, f) -> s.withColor(Formatting.DARK_RED)),  // brown
    DARK_AQUA   ('q', (s, f) -> s.withColor(Formatting.DARK_AQUA)),
    DARK_GREEN  ('e', (s, f) -> s.withColor(Formatting.DARK_GREEN)),
    DARK_BLUE   ('v', (s, f) -> s.withColor(Formatting.DARK_BLUE)), // navy
    BLACK       ('k', (s, f) -> s.withColor(Formatting.BLACK)),
 */
