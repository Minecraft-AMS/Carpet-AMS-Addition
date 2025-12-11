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

import club.mcams.carpet.utils.EntityUtil;
import club.mcams.carpet.utils.MinecraftServerUtil;

import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class FancyFakePlayerNameTeamController {
    public static void kickFakePlayerFromBotTeam(ServerPlayer player, String teamName) {
        Scoreboard scoreboard = EntityUtil.getEntityServer(player).getScoreboard();
        String playerName = player.getGameProfile().name();
        PlayerTeam currentTeam = scoreboard.getPlayersTeam(playerName);
        PlayerTeam targetTeam = scoreboard.getPlayerTeam(teamName);
        if (currentTeam != null && currentTeam.equals(targetTeam)) {
            scoreboard.removePlayerFromTeam(playerName, currentTeam);
        }
    }

    public static PlayerTeam addBotTeam(MinecraftServer server, String teamName) {
        return server.getScoreboard().addPlayerTeam(teamName);
    }

    public static void removeBotTeam(MinecraftServer server, String teamName) {
        if (MinecraftServerUtil.serverIsRunning()) {
            PlayerTeam fancyFakePlayerNameTeam = server.getScoreboard().getPlayerTeam(teamName);
            if (!Objects.equals(teamName, "false") && fancyFakePlayerNameTeam != null) {
                server.getScoreboard().removePlayerTeam(fancyFakePlayerNameTeam);
            }
        }
    }
}
