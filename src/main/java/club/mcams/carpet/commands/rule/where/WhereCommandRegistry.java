/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.commands.rule.where;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.where.GetPlayerPos;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Colors;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.compat.DimensionWrapper;
import club.mcams.carpet.utils.compat.LiteralTextUtil;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import com.mojang.brigadier.CommandDispatcher;

import static net.minecraft.server.command.CommandManager.argument;

public class WhereCommandRegistry {
    private static final Translator translator = new Translator("command.where");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("where")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandWhere))
            .then(argument("player", EntityArgumentType.player())
            .executes(context -> sendMessage(context.getSource().getPlayer(), EntityArgumentType.getPlayer(context, "player"))))
        );
    }

    private static int sendMessage(PlayerEntity senderPlayer, PlayerEntity targetPlayer) {
        senderPlayer.sendMessage(LiteralTextUtil.createColoredText(message(targetPlayer), Colors.AQUA), false);
        return 1;
    }

    private static String getPlayerName(PlayerEntity player) {
        return player.getName().getString();
    }

    private static String getCurrentPos(PlayerEntity player) {
        int[] pos = GetPlayerPos.getPos(player);
        return String.format("[ %d, %d, %d ]", pos[0], pos[1], pos[2]);
    }

    private static String getOtherPos(PlayerEntity player) {
        DimensionWrapper dimension = DimensionWrapper.of(player.getEntityWorld());
        int[] pos = GetPlayerPos.getPos(player);
        String otherPos = null;
        if (dimension.getValue() == World.NETHER) {
            otherPos = String.format("[ %d, %d, %d ]", pos[0] * 8, pos[1] * 8, pos[2] * 8);
        } else if (dimension.getValue() == World.OVERWORLD) {
            otherPos = String.format("[ %d, %d, %d ]", pos[0] / 8, pos[1] / 8, pos[2] / 8);
        }
        return otherPos;
    }

    private static String message(PlayerEntity player) {
        DimensionWrapper dimension = DimensionWrapper.of(player.getEntityWorld());
        String playerName = getPlayerName(player);
        String currentPos = getCurrentPos(player);
        String otherPos = getOtherPos(player);
        String message = null;
        if (dimension.getValue() == World.END) {
            message = String.format("[%s] %s @ %s", translator.tr("the_end").getString(), playerName, currentPos);
        } else if (dimension.getValue() == World.OVERWORLD) {
            message = String.format("[%s] %s @ %s -> %s", translator.tr("overworld").getString(), playerName, currentPos, otherPos);
        } else if (dimension.getValue() == World.NETHER) {
            message = String.format("[%s] %s @ %s -> %s", translator.tr("nether").getString(), playerName, currentPos, otherPos);
        }
        return message;
    }
}
