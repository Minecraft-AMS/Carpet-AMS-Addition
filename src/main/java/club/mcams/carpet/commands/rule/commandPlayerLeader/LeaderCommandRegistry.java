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

package club.mcams.carpet.commands.rule.commandPlayerLeader;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Colors;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;

import club.mcams.carpet.utils.compat.LiteralTextUtil;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LeaderCommandRegistry {
    private static final Translator translator = new Translator("command.leader");
    private static final int GLOWING_TIME = Integer.MAX_VALUE;
    private static final StatusEffectInstance HIGH_LIGHT = new StatusEffectInstance(StatusEffects.GLOWING, GLOWING_TIME);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("leader")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandPlayerLeader))
            .then(literal("add")
            .then(argument("player", EntityArgumentType.player())
            .executes(context -> add(context.getSource().getServer(), EntityArgumentType.getPlayer(context, "player")))))
            .then(literal("remove")
            .then(argument("player", EntityArgumentType.player())
            .executes(context -> remove(context.getSource().getServer(), EntityArgumentType.getPlayer(context, "player")))))
            .then(literal("help")
            .executes(context -> help(context.getSource().getPlayer())))
        );
    }

    private static int add(MinecraftServer server, PlayerEntity targetPlayer) {
        targetPlayer.addStatusEffect(HIGH_LIGHT);
        Messenger.sendServerMessage(
            server,
            String.format("%s %s", getPlayerName(targetPlayer), translator.tr("add").getString()),
            Colors.GRAY, false, false
        );
        return 1;
    }

    private static int remove(MinecraftServer server, PlayerEntity targetPlayer) {
        targetPlayer.removeStatusEffect(HIGH_LIGHT.getEffectType());
        Messenger.sendServerMessage(
            server,
            String.format("%s %s", getPlayerName(targetPlayer), translator.tr("remove").getString()),
            Colors.GRAY, false, false
        );
        return 1;
    }

    private static int help(PlayerEntity player) {
        String addHelp = translator.tr("help.add").getString();
        String removeHelp = translator.tr("help.remove").getString();
        player.sendMessage(
            LiteralTextUtil.createColoredText(
            addHelp + "\n" + removeHelp, Colors.GRAY, false, false
            ), false
        );
        return 1;
    }

    private static String getPlayerName(PlayerEntity player) {
        return player.getName().getString();
    }
}
