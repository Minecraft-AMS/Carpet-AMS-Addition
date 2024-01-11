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

package club.mcams.carpet.commands.rule.here;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Colors;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.compat.DimensionWrapper;
import club.mcams.carpet.helpers.rule.here.GetCommandSourcePos;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import com.mojang.brigadier.CommandDispatcher;

public class HereCommandRegistry {
    private static final Translator translator = new Translator("command.here");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("here")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandHere))
            .executes(context -> sendMessage(context.getSource(), context.getSource().getServer()))
        );
    }

    private static int sendMessage(ServerCommandSource source, MinecraftServer minecraftServer) {
        Messenger.sendServerMessage(minecraftServer, message(source), Colors.AQUA, false, false);
        return 1;
    }

    private static String getPlayerName(ServerCommandSource source) {
        return source.getName();
    }

    private static String getCurrentPos(ServerCommandSource source) {
        int[] pos = GetCommandSourcePos.getPos(source);
        return String.format("[ %d, %d, %d ]", pos[0], pos[1], pos[2]);
    }

    private static String getOtherPos(ServerCommandSource source) {
        DimensionWrapper dimension = DimensionWrapper.of(source.getWorld());
        int[] pos = GetCommandSourcePos.getPos(source);
        String otherPos = null;
        if (dimension.getValue() == World.NETHER) {
            otherPos = String.format("[ %d, %d, %d ]", pos[0] * 8, pos[1] * 8, pos[2] * 8);
        } else if (dimension.getValue() == World.OVERWORLD) {
            otherPos = String.format("[ %d, %d, %d ]", pos[0] / 8, pos[1] / 8, pos[2] / 8);
        }
        return otherPos;
    }

    private static String message(ServerCommandSource source) {
        DimensionWrapper dimension = DimensionWrapper.of(source.getWorld());
        String playerName = getPlayerName(source);
        String currentPos = getCurrentPos(source);
        String otherPos = getOtherPos(source);
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
