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

package carpetamsaddition.utils.compat;

import net.minecraft.commands.CommandSourceStack;
//#if MC<11904
//$$ import net.minecraft.Util;
//$$ import net.minecraft.network.chat.TextComponent;
//$$ import net.minecraft.network.chat.TranslatableComponent;
//#endif
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MessengerCompatFactory {
    public static MutableComponent carpetCompoundText(Object... fields) {
        //#if MC<11904
        //$$ return (BaseComponent) carpet.utils.Messenger.c(fields);
        //#else
        return (MutableComponent) carpet.utils.Messenger.c(fields);
        //#endif
    }

    public static MutableComponent literalText(String text) {
        //#if MC<11904
        //$$ return new TextComponent(text);
        //#else
        return Component.literal(text);
        //#endif
    }

    public static MutableComponent translatableText(String key, Object... args) {
        //#if MC<11904
        //$$ return new TranslatableComponent(key, args);
        //#else
        return Component.translatable(key, args);
        //#endif
    }

    public static void sendFeedback(CommandSourceStack source, MutableComponent text, boolean broadcastToOps) {
        //#if MC<=11904
        //$$ source.sendSuccess(text, broadcastToOps);
        //#else
        source.sendSuccess(() -> text, broadcastToOps);
        //#endif
    }

    public static void sendPlayerMessage(ServerPlayer player, MutableComponent text, boolean overlay) {
        //#if MC<11904
        //$$ player.sendMessage(text, Util.NIL_UUID);
        //#else
        player.sendSystemMessage(text, overlay);
        //#endif
    }

    public static void sendPlayerMessage(Player player, MutableComponent text) {
        //#if MC>=260102
        //$$ player.sendSystemMessage(text);
        //#elseif MC<11904
        //$$ player.sendMessage(text, Util.NIL_UUID);
        //#else
        player.displayClientMessage(text, false);
        //#endif
    }

    public static void sendServerMessage(MinecraftServer server, MutableComponent text) {
        //#if MC<11904
        //$$ server.sendMessage(text, Util.NIL_UUID);
        //#else
        server.sendSystemMessage(text);
        //#endif
    }
}
