/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.observers.rule.fancyFakePlayerName;

import carpet.patches.EntityPlayerMPFake;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyFakePlayerNameTeamController;
import club.mcams.carpet.helpers.rule.fancyFakePlayerName.FancyNameHelper;
import club.mcams.carpet.settings.RuleObserver;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Objects;

public class FancyFakePlayerNameRuleObserver extends RuleObserver<String> {
    @Override
    public void onValueChange(String oldValue, String newValue) {
        MinecraftServer server = AmsServer.minecraftServer;
        if (server != null) {
            FancyFakePlayerNameTeamController.removeBotTeam(server, oldValue);
            if (!Objects.equals(newValue, "false")) {
                List<ServerPlayerEntity> playerEntities = server.getPlayerManager().getPlayerList();
                for (ServerPlayerEntity player : playerEntities) {
                    if (player instanceof EntityPlayerMPFake && !((EntityPlayerMPFake) player).isAShadow) {
                        FancyNameHelper.addBotTeamNamePrefix(player, newValue);
                    }
                }
            }
        }
    }
}
