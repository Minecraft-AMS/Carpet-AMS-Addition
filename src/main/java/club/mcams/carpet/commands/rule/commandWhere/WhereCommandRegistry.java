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
import club.mcams.carpet.helpers.rule.commandHere_commandWhere.CommandHereWhereHelper;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.compat.DimensionWrapper;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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

    private static void sendWhoGetWhoMessage(MinecraftServer minecraftServer, PlayerEntity senderPlayer, PlayerEntity targetPlayer) {
        String senderPlayerName = getPlayerName(senderPlayer);
        String targetPlayerName = getPlayerName(targetPlayer);
        String message = translator.tr("who_get_who", senderPlayerName, targetPlayerName).getString();
        Messenger.sendServerMessage(
            minecraftServer,
            Messenger.s(message).
            setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true))
        );
    }

    private static void highlightPlayer(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600));
    }

    private static String getPlayerName(PlayerEntity player) {
        return player.getName().getString();
    }

    private static String getCurrentPos(PlayerEntity player) {
        int[] pos = CommandHereWhereHelper.getPos(player);
        return String.format("%d, %d, %d", pos[0], pos[1], pos[2]);
    }

    private static String getOtherPos(PlayerEntity player) {
        DimensionWrapper dimension = DimensionWrapper.of(player.getEntityWorld());
        int[] pos = CommandHereWhereHelper.getPos(player);
        String otherPos = null;
        if (dimension.getValue() == World.NETHER) {
            otherPos = String.format("%d, %d, %d", pos[0] * 8, pos[1], pos[2] * 8);
        } else if (dimension.getValue() == World.OVERWORLD) {
            otherPos = String.format("%d, %d, %d", pos[0] / 8, pos[1], pos[2] / 8);
        }
        return otherPos;
    }

    private static Text message(PlayerEntity player) {
        DimensionWrapper dimension = DimensionWrapper.of(player.getEntityWorld());
        String playerName = getPlayerName(player);
        String currentPos = getCurrentPos(player);
        String otherPos = getOtherPos(player);
        Text message = Messenger.s("Unknown dimension").formatted(Formatting.RED);
        if (dimension.getValue() == World.END) {
            message = Messenger.s(
                String.format("§d[%s] §e%s §b@ §d[ %s ]", translator.tr("the_end").getString(), playerName, currentPos))
                .append(copyButton(currentPos, Formatting.LIGHT_PURPLE)
            );
        } else if (dimension.getValue() == World.OVERWORLD) {
            message = Messenger.s(
                String.format("§2[%s] §e%s §b@ §2[ %s ] §b-> §4[ %s ]", translator.tr("overworld").getString(), playerName, currentPos, otherPos))
                .append(copyButton(currentPos, Formatting.GREEN)).append(copyButton(otherPos, Formatting.DARK_RED)
            );
        } else if (dimension.getValue() == World.NETHER) {
            message = Messenger.s(
                String.format("§4[%s] §e%s §b@ §4[ %s ] §b-> §2[ %s ]", translator.tr("nether").getString(), playerName, currentPos, otherPos))
                .append(copyButton(currentPos, Formatting.DARK_RED)).append(copyButton(otherPos, Formatting.GREEN)
            );
        }
        return message;
    }

    private static Text copyButton(String copyText, Formatting buttonColor) {
        Text hoverText = Messenger.s(translator.tr("copy").getString()).formatted(Formatting.YELLOW);
        String copyCoordText = copyText.replace(",", ""); // 1, 0, -24 -> 1 0 -24
        if (buttonColor == Formatting.LIGHT_PURPLE) {
            hoverText = Messenger.s(translator.tr("the_end_button_hover").getString()).formatted(Formatting.YELLOW);
        } else if (buttonColor == Formatting.GREEN) {
            hoverText = Messenger.s(translator.tr("overworld_button_hover").getString()).formatted(Formatting.YELLOW);
        } else if (buttonColor == Formatting.DARK_RED) {
            hoverText = Messenger.s(translator.tr("nether_button_hover").getString()).formatted(Formatting.YELLOW);
        }
        return
            Messenger.s(" [C]").setStyle(
                Style.EMPTY.withColor(buttonColor).withBold(true).
                withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, copyCoordText)).
                withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, hoverText))
            );
    }
}
