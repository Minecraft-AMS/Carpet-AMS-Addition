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

import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.PlayerUtil;
import carpetamsaddition.utils.messenger.Messenger;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
//#if MC>=260200
//$$ import net.minecraft.world.scores.TeamColor;
//$$ import java.util.Optional;
//#endif

import java.util.Objects;

public class FancyNameHelper {
    public static void addBotTeamNamePrefix(ServerPlayer player, String teamName) {
        MinecraftServer server = EntityUtil.getEntityServer(player);

        if (server == null) {
            return;
        }

        Scoreboard scoreboard = server.getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);

        if (team == null) {
            team = FancyFakePlayerNameTeamController.addBotTeam(server, teamName);
            team.setPlayerPrefix(Messenger.f(Messenger.s("[" + teamName + "] "), Layout.BOLD));
            team.setColor(
                //#if MC>=260200
                //$$ Optional.of(TeamColor.DARK_GREEN)
                //#else
                Layout.DARK_GREEN.getFormatting()
                //#endif
            );
        }

        String playerName = PlayerUtil.getName(player);

        if (scoreboard.getPlayersTeam(playerName) == team) {
            return;
        }

        scoreboard.addPlayerToTeam(playerName, team);
    }

    public static String addBotNameSuffix(final CommandContext<?> context, final String name, String teamName) {
        String playerName = StringArgumentType.getString(context, name);

        if (!Objects.equals(name, "player") || Objects.equals(teamName, "false")) {
            return playerName;
        }

        return playerName + "_" + teamName;
    }
}
