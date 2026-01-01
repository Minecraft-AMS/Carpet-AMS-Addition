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

package club.mcams.carpet.fuzz;

import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;

import net.minecraft.text.BaseText;

import org.jetbrains.annotations.NotNull;

public class InvokeFuzzModCommand {
    private static final Translator tr = new Translator("fuzz.command");

    @NotNull
    public static BaseText highlightCoordButton(String posText) {
        String cmd = "/coordCompass set " + posText.replace(",", "");

        return Messenger.f((BaseText) Messenger.s(" [+H]").setStyle(Messenger.simpleCmdButtonStyle(cmd, tr.tr("highlightCoordButtonHoverText"), Layout.YELLOW)), Layout.YELLOW, Layout.BOLD);
    }
}
