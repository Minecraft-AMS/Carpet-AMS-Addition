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
import net.minecraft.util.Util;

public class MessengerCompatFactory {
    public static BaseText CarpetCompoundText(Object... fields) {
        //#if MC>=11900
        //$$ return (MutableText) carpet.utils.Messenger.c(fields);
        //#else
        return carpet.utils.Messenger.c(fields);
        //#endif
    }

    //#if MC<11900
    public static LiteralText LiteralText(String text) {
        return new LiteralText(text);
    }
    //#else
    //$$ public static MutableText LiteralText(String text) {
    //$$     return Text.literal(text);
    //$$ }
    //#endif

    public static void sendFeedBack(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        //#if MC>=12000
        //$$ source.sendFeedback(() -> text, broadcastToOps);
        //#else
        source.sendFeedback(text, broadcastToOps);
        //#endif
    }

    // Send system message to server
    public static void sendSystemMessage(MinecraftServer server, BaseText text) {
        //#if MC<11900
        server.sendSystemMessage(text, Util.NIL_UUID);
        //#else
        //$$ server.sendMessage(text);
        //#endif
    }

    // Send system message to player
    public static void sendSystemMessage(PlayerEntity player, BaseText text) {
        //#if MC<11900
        player.sendSystemMessage(text, Util.NIL_UUID);
        //#else
        //$$ player.sendMessage(text, false);
        //#endif
    }

    public static BaseText TranslatableText(String key, Object... args) {
        //#if MC>=11900
        //$$ return Text.translatable(key, args);
        //#else
        return new TranslatableText(key, args);
        //#endif
    }
}
