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

package carpetamsaddition.utils.compat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;

public class MessengerCompatFactory {
    public static MutableComponent CarpetCompoundText(Object... fields) {
        return (MutableComponent) carpet.utils.Messenger.c(fields);
    }

    public static MutableComponent LiteralText(String text) {
        return Component.literal(text);
    }

    public static void sendFeedBack(CommandSourceStack source, MutableComponent text, boolean broadcastToOps) {
        source.sendSuccess(() -> text, broadcastToOps);
    }

    // Send system message to server
    public static void sendSystemMessage(MinecraftServer server, Component text) {
        server.sendSystemMessage(text);
    }

    // Send system message to player
    public static void sendSystemMessage(Player player, Component text) {
        player.displayClientMessage(text, false);
    }

    public static MutableComponent TranslatableText(String key, Object... args) {
        return Component.translatable(key, args);
    }
}
