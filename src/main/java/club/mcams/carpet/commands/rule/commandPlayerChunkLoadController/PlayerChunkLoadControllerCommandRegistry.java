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

package club.mcams.carpet.commands.rule.commandPlayerChunkLoadController;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.helpers.rule.commandPlayerChunkLoadController.ChunkLoading;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;

public class PlayerChunkLoadControllerCommandRegistry {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("playerChunkLoading")
            .requires((player) -> CommandHelper.canUseCommand(player, AmsServerSettings.commandPlayerChunkLoadController))
            .executes((c) -> listPlayerInteractions(c.getSource(), c.getSource().getName()))
            .then(argument("boolean", BoolArgumentType.bool())
            .executes((c) -> setPlayerInteraction(c.getSource(), c.getSource().getName(), getBool(c, "boolean"))))
        );
    }

    private static int setPlayerInteraction(ServerCommandSource source, String playerName, boolean b) {
        ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        ChunkLoading.setPlayerLoading(player, b);
        if (player == null) {
            Messenger.sendServerMessage(
                AmsServer.minecraftServer, Messenger.s("No player specified").
                setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true))
            );
            return 0;
        } else {
            player.sendMessage(
                Messenger.s((playerName + " chunk loading " + b)).
                setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)),
                false
            );
            return 1;
        }
    }

    private static int listPlayerInteractions(ServerCommandSource source, String playerName) {
        ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        if (player == null) {
            Messenger.sendServerMessage(
                AmsServer.minecraftServer, Messenger.s("No player specified").
                setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true))
            );
            return 0;
        }
        boolean playerInteractions = !ChunkLoading.isPlayerUnLoading(player);
        if (playerInteractions) {
            player.sendMessage(
                Messenger.s((playerName + " chunk loading: true")).
                setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)),
                false
            );
        } else {
            player.sendMessage(
                Messenger.s((playerName + " chunk loading: false")).
                setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true)),
                false
            );
        }
        return 1;
    }
}