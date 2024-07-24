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
    public static void addBotTeamNamePrefix(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server != null) {
            Scoreboard scoreboard = player.getServer().getScoreboard();
            Team team = scoreboard.getTeam(AmsServerSettings.fancyFakePlayerName);
            if (team == null) {
                team = FancyFakePlayerNameTeamController.addBotTeam(player.getServer());
                team.setPrefix(Messenger.s("[bot] ").formatted(Formatting.BOLD));
                team.setColor(Formatting.DARK_GREEN);
            }
            scoreboard.addPlayerToTeam(player.getGameProfile().getName(), team);
        }
    }

    public static String addBotNameSuffix(final CommandContext<?> context, final String name) {
        final String SUFFIX = "_bot";
        //#if MC>=11700
        //$$ String playerName = StringArgumentType.getString(context, name);
        //$$ if (!name.equals("player")) {
        //$$ 	return playerName;
        //$$ }
        //#else
        String playerName = StringArgumentType.getString(context, name);
        //#endif
        if (!Objects.equals(AmsServerSettings.fancyFakePlayerName, "false")) {
            playerName = playerName + SUFFIX;
        }
        return playerName;
    }
}
