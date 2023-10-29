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

package club.mcams.carpet.commands;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.ChunkLoading;
import club.mcams.carpet.util.CommandHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;

import carpet.utils.Messenger;

public class commandChunkLoadingCommandRegistry {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("chunkloading")
                .requires((player) -> CommandHelper.canUseCommand(player, AmsServerSettings.commandChunkLoading))
                .executes((c) -> listPlayerInteractions(c.getSource(), c.getSource().getName()))
                .then(argument("boolean", BoolArgumentType.bool()).
                        executes((c) -> setPlayerInteraction(c.getSource(), c.getSource().getName(), getBool(c, "boolean")))
                ));
    }

    private static int setPlayerInteraction(ServerCommandSource source, String playerName, boolean b) {
        PlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        if (player == null) {
            Messenger.m(source, "r No player specified");
            return 0;
        }
        ChunkLoading.setPlayerInteraction(playerName, b, true);
        Messenger.m(source, "w Set interaction ", "g " + "chunk loading", "w  to ", "g " + b);
        return 1;
    }

    private static int listPlayerInteractions(ServerCommandSource source, String playerName) {
        PlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        if (player == null) {
            Messenger.m(source, "r No player specified");
            return 0;
        }
        boolean playerInteractions = ChunkLoading.onlinePlayerMap.getOrDefault(playerName, true);

        if (playerInteractions) Messenger.m(source, "w " + "chunk loading" + ": ", "g true");
        else Messenger.m(source, "w " + "chunk loading" + ": ", "g false");
        return 1;
    }
}