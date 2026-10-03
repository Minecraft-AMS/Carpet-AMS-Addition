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

package carpetamsaddition.commands.rule.commandPlayerChunkLoadController;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messenger.Messenger;
import carpetamsaddition.helpers.rule.commandPlayerChunkLoadController.ChunkLoading;

import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;

public class PlayerChunkLoadControllerCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.playerChunkLoading");

    @Override
    public void define(CommandBuilder command) {
        command.name("playerChunkLoading")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandPlayerChunkLoadController)
            .executes(c -> listPlayerInteractions(c.source(), c.source().getTextName()))
            .route(Arguments.bool("boolean"))
            .executes(c -> setPlayerInteraction(c.source(), c.source().getTextName(), c.get(Arguments.bool("boolean"))))
            .route("help")
            .executes(c -> help(c.source()));
    }

    private static int setPlayerInteraction(CommandSourceStack source, String playerName, boolean b) {
        Player player = source.getServer().getPlayerList().getPlayerByName(playerName);
        ChunkLoading.setPlayerInteraction(playerName, b, true);

        if (player == null) {
            Messenger.tell(source, Messenger.f(tr.tr("no_player_specified"), Layout.RED, Layout.BOLD));
            return 0;
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("set", playerName, String.valueOf(b)), Layout.LIGHT_PURPLE, Layout.BOLD));
            return 1;
        }
    }

    private static int listPlayerInteractions(CommandSourceStack source, String playerName) {
        boolean playerInteractions = ChunkLoading.onlinePlayerMap.getOrDefault(playerName, true);
        Player player = source.getServer().getPlayerList().getPlayerByName(playerName);

        if (player == null) {
            Messenger.tell(source, Messenger.f(tr.tr("no_player_specified"), Layout.RED, Layout.BOLD));
            return 0;
        }

        if (playerInteractions) {
            Messenger.tell(source, Messenger.f(tr.tr("chunk_loading_true"), Layout.LIGHT_PURPLE, Layout.BOLD));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("chunk_loading_false"), Layout.LIGHT_PURPLE, Layout.BOLD));
        }

        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(tr.tr("help"), Layout.GRAY));
        return 1;
    }
}
