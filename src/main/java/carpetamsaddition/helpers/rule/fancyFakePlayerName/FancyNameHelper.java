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

package carpetamsaddition.helpers.rule.fancyFakePlayerName;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.utils.Messenger;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.ChatFormatting;

import java.util.Objects;

public class FancyNameHelper {
    public static void addBotTeamNamePrefix(ServerPlayer player, String teamName) {
        MinecraftServer server = EntityUtil.getEntityServer(player);
        if (server != null) {
            Scoreboard scoreboard = server.getScoreboard();
            PlayerTeam team = scoreboard.getPlayerTeam(teamName);

            if (team == null) {
                team = FancyFakePlayerNameTeamController.addBotTeam(server, teamName);
                team.setPlayerPrefix(Messenger.s(String.format("[%s] ", teamName)).withStyle(ChatFormatting.BOLD));
                team.setColor(ChatFormatting.DARK_GREEN);
            }

            String playerName = player.getGameProfile().name();
            PlayerTeam currentTeam = scoreboard.getPlayersTeam(playerName);

            if (currentTeam != null && currentTeam != team) {
                scoreboard.removePlayerFromTeam(playerName, currentTeam);
            }

            if (currentTeam != team) {
                scoreboard.addPlayerToTeam(playerName, team);
            }
        }
    }

    public static String addBotNameSuffix(final CommandContext<?> context, final String name, String teamName) {
        final String SUFFIX = "_" + CarpetAMSAdditionSettings.fancyFakePlayerName;
        String playerName = StringArgumentType.getString(context, name);

        if (!name.equals("player")) {
        	return playerName;
        }

        if (!Objects.equals(teamName, "false")) {
            playerName = playerName + SUFFIX;
        }

        return playerName;
    }
}
