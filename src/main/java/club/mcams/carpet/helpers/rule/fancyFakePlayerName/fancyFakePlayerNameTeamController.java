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

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class fancyFakePlayerNameTeamController {
    public static void kickFakePlayerFromBotTeam(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            Scoreboard scoreboard = player.getServer().getScoreboard();
            Team team = scoreboard.getTeam(AmsServerSettings.fancyFakePlayerName);
            if (team != null) {
                team.getPlayerList().remove(player.getGameProfile().getName());
            }
        }
    }

    public static Team addBotTeam(MinecraftServer server) {
        return server.getScoreboard().addTeam(AmsServerSettings.fancyFakePlayerName);
    }

    public static void removeBotTeam(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            Scoreboard scoreboard = player.getServer().getScoreboard();
            Team team = scoreboard.getTeam(AmsServerSettings.fancyFakePlayerName);
            if (team != null) {
                scoreboard.removeTeam(team);
            }
        }
    }

    public static void removeBotTeam(MinecraftServer server) {
        Team fancyFakePlayerNameTeam = server.getScoreboard().getTeam(AmsServerSettings.fancyFakePlayerName);
        if (!Objects.equals(AmsServerSettings.fancyFakePlayerName, "false") && fancyFakePlayerNameTeam != null) {
            server.getScoreboard().removeTeam(fancyFakePlayerNameTeam);
        }
    }
}
