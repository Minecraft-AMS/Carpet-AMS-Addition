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

package club.mcams.carpet.helpers.rule.fancyFakePlayerName;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.EntityUtil;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class FancyNameHelper {
    public static void addBotTeamNamePrefix(ServerPlayerEntity player, String teamName) {
        MinecraftServer server = EntityUtil.getEntityServer(player);
        if (server != null) {
            Scoreboard scoreboard = server.getScoreboard();
            Team team = scoreboard.getTeam(teamName);

            if (team == null) {
                team = FancyFakePlayerNameTeamController.addBotTeam(server, teamName);
                team.setPrefix(Messenger.s(String.format("[%s] ", teamName)).formatted(Formatting.BOLD));
                team.setColor(Formatting.DARK_GREEN);
            }

            String playerName = player.getGameProfile().getName();
            Team currentTeam = scoreboard.getPlayerTeam(playerName);

            if (currentTeam != null && currentTeam != team) {
                scoreboard.removePlayerFromTeam(playerName, currentTeam);
            }

            if (currentTeam != team) {
                scoreboard.addPlayerToTeam(playerName, team);
            }
        }
    }

    public static String addBotNameSuffix(final CommandContext<?> context, final String name, String teamName) {
        final String SUFFIX = "_" + AmsServerSettings.fancyFakePlayerName;
        //#if MC>=11700
        //$$ String playerName = StringArgumentType.getString(context, name);
        //$$ if (!name.equals("player")) {
        //$$ 	return playerName;
        //$$ }
        //#else
        String playerName = StringArgumentType.getString(context, name);
        //#endif

        if (!Objects.equals(teamName, "false")) {
            playerName = playerName + SUFFIX;
        }

        return playerName;
    }
}
