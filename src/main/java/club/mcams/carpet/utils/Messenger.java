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

package club.mcams.carpet.utils;

import club.mcams.carpet.utils.compat.MessengerCompatFactory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.text.*;

import java.util.Objects;

public class Messenger {
    // Compound Text
    public static BaseText c(Object... fields) {
        return MessengerCompatFactory.CarpetCompoundText(fields);
    }

    // Simple Text
    public static BaseText s(Object text) {
        return MessengerCompatFactory.LiteralText(text.toString());
    }

    // Simple Text with formatting
    public static BaseText s(Object text, Formatting textFormatting) {
        return formatting(s(text), textFormatting);
    }

    // Translation Text
    public static BaseText tr(String key, Object... args) {
        return MessengerCompatFactory.TranslatableText(key, args);
    }

    public static BaseText copy(BaseText text) {
        return (BaseText) text.shallowCopy();
    }

    private static void __tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        MessengerCompatFactory.sendFeedBack(source, text, broadcastToOps);
    }

    public static void tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        __tell(source, text, broadcastToOps);
    }

    public static void tell(ServerCommandSource source, BaseText text) {
        tell(source, text, false);
    }

    public static Text endl() {
        return Messenger.s("\n");
    }

    public static BaseText formatting(BaseText text, Formatting... formattings) {
        text.formatted(formattings);
        return text;
    }

    public static void sendServerMessage(MinecraftServer server, Text text) {
        Objects.requireNonNull(server, "Server is null, message not delivered !");
        MessengerCompatFactory.sendSystemMessage(server, text);
        server.getPlayerManager().getPlayerList().forEach(player -> MessengerCompatFactory.sendSystemMessage(player, text));
    }
}
