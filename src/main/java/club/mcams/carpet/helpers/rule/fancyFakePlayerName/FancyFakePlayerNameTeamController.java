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

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class FancyFakePlayerNameTeamController {
    public static void kickFakePlayerFromBotTeam(ServerPlayerEntity player, String teamName) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            Scoreboard scoreboard = player.getServer().getScoreboard();
            Team team = scoreboard.getTeam(teamName);
            if (team != null) {
                team.getPlayerList().remove(player.getGameProfile().getName());
            }
        }
    }

    public static Team addBotTeam(MinecraftServer server, String teamName) {
        return server.getScoreboard().addTeam(teamName);
    }

    public static void removeBotTeam(MinecraftServer server, String teamName) {
        Team fancyFakePlayerNameTeam = server.getScoreboard().getTeam(teamName);
        if (!Objects.equals(teamName, "false") && fancyFakePlayerNameTeam != null) {
            server.getScoreboard().removeTeam(fancyFakePlayerNameTeam);
        }
    }
}
