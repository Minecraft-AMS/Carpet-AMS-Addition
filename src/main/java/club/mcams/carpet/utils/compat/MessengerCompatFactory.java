/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.utils.compat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;

public class MessengerCompatFactory {
    public static MutableText CarpetCompoundText(Object... fields) {
        return (MutableText) carpet.utils.Messenger.c(fields);
    }

    public static MutableText LiteralText(String text) {
        return Text.literal(text);
    }

    public static void sendFeedBack(ServerCommandSource source, MutableText text, boolean broadcastToOps) {
        source.sendFeedback(() -> text, broadcastToOps);
    }

    // Send system message to server
    public static void sendSystemMessage(MinecraftServer server, Text text) {
        server.sendMessage(text);
    }

    // Send system message to player
    public static void sendSystemMessage(PlayerEntity player, Text text) {
        player.sendMessage(text, false);
    }

    public static MutableText TranslatableText(String key, Object... args) {
        return Text.translatable(key, args);
    }
}
