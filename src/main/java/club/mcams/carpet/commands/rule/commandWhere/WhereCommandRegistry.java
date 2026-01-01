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

package club.mcams.carpet.commands.rule.commandWhere;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.fuzz.InvokeFuzzModCommand;
import club.mcams.carpet.helpers.rule.commandHere_commandWhere.CommandHereWhereHelper;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.*;
import club.mcams.carpet.utils.compat.DimensionWrapper;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.world.World;

import com.mojang.brigadier.CommandDispatcher;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;

public class WhereCommandRegistry {
    private static final Translator tr = new Translator("command.where");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("where")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandWhere))
            .then(argument("player", EntityArgumentType.player())
            .executes(context -> sendMessage(
                context.getSource().getServer(),
                context.getSource().getPlayer(),
                EntityArgumentType.getPlayer(context, "player")
            )))
        );
    }

    private static int sendMessage(MinecraftServer minecraftServer, PlayerEntity senderPlayer, PlayerEntity targetPlayer) {
        senderPlayer.sendMessage(message(targetPlayer), false);
        sendWhoGetWhoMessage(minecraftServer, senderPlayer, targetPlayer);
        highlightPlayer(targetPlayer);
        return 1;
    }

    // 用于与Leader命令联动
    public static void sendMessage(PlayerEntity targetPlayer) {
        if (MinecraftServerUtil.serverIsRunning() && targetPlayer != null) {
            Messenger.sendServerMessage(MinecraftServerUtil.getServer(), message(targetPlayer));
        }
    }

    private static void sendWhoGetWhoMessage(MinecraftServer minecraftServer, PlayerEntity senderPlayer, PlayerEntity targetPlayer) {
        String senderPlayerName = getPlayerName(senderPlayer);
        String targetPlayerName = getPlayerName(targetPlayer);
        String message = tr.tr("who_get_who", senderPlayerName, targetPlayerName).getString();
        Messenger.sendServerMessage(minecraftServer, Messenger.f(Messenger.s(message), Layout.GRAY, Layout.ITALIC));
    }

    private static void highlightPlayer(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600));
    }

    private static String getPlayerName(PlayerEntity player) {
        return player.getGameProfile().getName();
    }

    private static String getCurrentPos(PlayerEntity player) {
        int[] pos = CommandHereWhereHelper.getPos(player);
        return String.format("%d, %d, %d", pos[0], pos[1], pos[2]);
    }

    private static String getOtherPos(PlayerEntity player) {
        DimensionWrapper dimension = DimensionWrapper.of(EntityUtil.getEntityWorld(player));
        int[] pos = CommandHereWhereHelper.getPos(player);
        String otherPos = null;

        if (dimension.getValue() == World.NETHER) {
            otherPos = String.format("%d, %d, %d", pos[0] * 8, pos[1], pos[2] * 8);
        } else if (dimension.getValue() == World.OVERWORLD) {
            otherPos = String.format("%d, %d, %d", pos[0] / 8, pos[1], pos[2] / 8);
        }

        return otherPos;
    }

    private static BaseText message(PlayerEntity player) {
        DimensionWrapper dimension = DimensionWrapper.of(EntityUtil.getEntityWorld(player));
        String playerName = getPlayerName(player);
        String currentPos = getCurrentPos(player);
        String otherPos = getOtherPos(player);
        BaseText message = Messenger.f(Messenger.s("Unknown dimension"), Layout.RED);
        if (dimension.getValue() == World.END) {
            message = (BaseText) Messenger.s(
                String.format("§d[%s] §e%s §b@ §d[ %s ]", tr.tr("the_end").getString(), playerName, currentPos))
                .append(copyButton(currentPos, Layout.LIGHT_PURPLE)).append(InvokeFuzzModCommand.highlightCoordButton(currentPos));
        } else if (dimension.getValue() == World.OVERWORLD) {
            message = (BaseText) Messenger.s(
                String.format("§2[%s] §e%s §b@ §2[ %s ] §b-> §4[ %s ]", tr.tr("overworld").getString(), playerName, currentPos, otherPos))
                .append(copyButton(currentPos, Layout.GREEN)).append(copyButton(otherPos, Layout.DARK_RED)).append(InvokeFuzzModCommand.highlightCoordButton(currentPos));
        } else if (dimension.getValue() == World.NETHER) {
            message = (BaseText) Messenger.s(
                String.format("§4[%s] §e%s §b@ §4[ %s ] §b-> §2[ %s ]", tr.tr("nether").getString(), playerName, currentPos, otherPos))
                .append(copyButton(currentPos, Layout.DARK_RED)).append(copyButton(otherPos, Layout.GREEN)).append(InvokeFuzzModCommand.highlightCoordButton(otherPos));
        }
        return message;
    }

    private static BaseText copyButton(String copyText, Layout buttonColor) {
        String copyCoordText = copyText.replace(",", ""); // 1, 0, -24 -> 1 0 -24
        BaseText hoverText = null;

        if (buttonColor == Layout.LIGHT_PURPLE) {
            hoverText = tr.tr("the_end_button_hover");
        } else if (buttonColor == Layout.GREEN) {
            hoverText = tr.tr("overworld_button_hover");
        } else if (buttonColor == Layout.DARK_RED) {
            hoverText = tr.tr("nether_button_hover");
        }

        return Messenger.f((BaseText) Messenger.s(" [C]").setStyle(Messenger.simpleCopyButtonStyle(copyCoordText, Objects.requireNonNull(hoverText), Layout.YELLOW)), Layout.BOLD, buttonColor);
    }
}
