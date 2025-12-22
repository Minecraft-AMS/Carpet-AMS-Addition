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

package carpetamsaddition.commands.rule.commandWhere;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.fuzz.InvokeFuzzModCommand;
import carpetamsaddition.helpers.rule.commandHere_commandWhere.CommandHereWhereHelper;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.compat.DimensionWrapper;

import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import com.mojang.brigadier.CommandDispatcher;

import java.util.Objects;

import static net.minecraft.commands.Commands.argument;

public class WhereCommandRegistry {
    private static final Translator tr = new Translator("command.where");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("where")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandWhere))
            .then(argument("player", EntityArgument.player())
            .executes(context -> sendMessage(
                context.getSource().getServer(),
                context.getSource().getPlayerOrException(),
                EntityArgument.getPlayer(context, "player")
            )))
        );
    }

    private static int sendMessage(MinecraftServer minecraftServer, Player senderPlayer, Player targetPlayer) {
        senderPlayer.displayClientMessage(message(targetPlayer), false);
        sendWhoGetWhoMessage(minecraftServer, senderPlayer, targetPlayer);
        highlightPlayer(targetPlayer);
        return 1;
    }

    // 用于与Leader命令联动
    public static void sendMessage(Player targetPlayer) {
        if (MinecraftServerUtil.serverIsRunning() && targetPlayer != null) {
            Messenger.sendServerMessage(MinecraftServerUtil.getServer(), message(targetPlayer));
        }
    }

    private static void sendWhoGetWhoMessage(MinecraftServer minecraftServer, Player senderPlayer, Player targetPlayer) {
        String senderPlayerName = getPlayerName(senderPlayer);
        String targetPlayerName = getPlayerName(targetPlayer);
        String message = tr.tr("who_get_who", senderPlayerName, targetPlayerName).getString();
        Messenger.sendServerMessage(
            minecraftServer,
            Messenger.s(message).
            setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true))
        );
    }

    private static void highlightPlayer(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 600));
    }

    private static String getPlayerName(Player player) {
        return player.getGameProfile().name();
    }

    private static String getCurrentPos(Player player) {
        int[] pos = CommandHereWhereHelper.getPos(player);
        return String.format("%d, %d, %d", pos[0], pos[1], pos[2]);
    }

    private static String getOtherPos(Player player) {
        DimensionWrapper dimension = DimensionWrapper.of(EntityUtil.getEntityWorld(player));
        int[] pos = CommandHereWhereHelper.getPos(player);
        String otherPos = null;
        if (dimension.getValue() == Level.NETHER) {
            otherPos = String.format("%d, %d, %d", pos[0] * 8, pos[1], pos[2] * 8);
        } else if (dimension.getValue() == Level.OVERWORLD) {
            otherPos = String.format("%d, %d, %d", pos[0] / 8, pos[1], pos[2] / 8);
        }
        return otherPos;
    }

    private static Component message(Player player) {
        DimensionWrapper dimension = DimensionWrapper.of(EntityUtil.getEntityWorld(player));
        String playerName = getPlayerName(player);
        String currentPos = getCurrentPos(player);
        String otherPos = getOtherPos(player);
        Component message = Messenger.s("Unknown dimension").withStyle(ChatFormatting.RED);
        if (dimension.getValue() == Level.END) {
            message = Messenger.s(
                String.format("§d[%s] §e%s §b@ §d[ %s ]", tr.tr("the_end").getString(), playerName, currentPos))
                .append(copyButton(currentPos, ChatFormatting.LIGHT_PURPLE)).append(InvokeFuzzModCommand.highlightCoordButton(currentPos));
        } else if (dimension.getValue() == Level.OVERWORLD) {
            message = Messenger.s(
                String.format("§2[%s] §e%s §b@ §2[ %s ] §b-> §4[ %s ]", tr.tr("overworld").getString(), playerName, currentPos, otherPos))
                .append(copyButton(currentPos, ChatFormatting.GREEN)).append(copyButton(otherPos, ChatFormatting.DARK_RED)).append(InvokeFuzzModCommand.highlightCoordButton(currentPos));
        } else if (dimension.getValue() == Level.NETHER) {
            message = Messenger.s(
                String.format("§4[%s] §e%s §b@ §4[ %s ] §b-> §2[ %s ]", tr.tr("nether").getString(), playerName, currentPos, otherPos))
                .append(copyButton(currentPos, ChatFormatting.DARK_RED)).append(copyButton(otherPos, ChatFormatting.GREEN)).append(InvokeFuzzModCommand.highlightCoordButton(otherPos));
        }
        return message;
    }

    private static Component copyButton(String copyText, ChatFormatting buttonColor) {
        String copyCoordText = copyText.replace(",", ""); // 1, 0, -24 -> 1 0 -24
        Component hoverText = null;

        if (buttonColor == ChatFormatting.LIGHT_PURPLE) {
            hoverText = tr.tr("the_end_button_hover");
        } else if (buttonColor == ChatFormatting.GREEN) {
            hoverText = tr.tr("overworld_button_hover");
        } else if (buttonColor == ChatFormatting.DARK_RED) {
            hoverText = tr.tr("nether_button_hover");
        }

        return
            Messenger.s(" [C]")
            .setStyle(Messenger.simpleCopyButtonStyle(copyCoordText, Objects.requireNonNull(hoverText), ChatFormatting.YELLOW))
            .withStyle(ChatFormatting.BOLD, buttonColor);
    }
}
