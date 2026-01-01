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

package club.mcams.carpet.commands.rule.commandHere;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.fuzz.InvokeFuzzModCommand;
import club.mcams.carpet.helpers.rule.commandHere_commandWhere.CommandHereWhereHelper;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.compat.DimensionWrapper;

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

public class HereCommandRegistry {
    private static final Translator tr = new Translator("command.here");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("here")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandHere))
            .executes(context -> sendMessage(
                context.getSource(),
                context.getSource().getServer(),
                context.getSource().getPlayer()
            ))
        );
    }

    private static int sendMessage(ServerCommandSource source, MinecraftServer minecraftServer, PlayerEntity player) {
        Messenger.sendServerMessage(minecraftServer, message(source));
        highlightPlayer(player);
        return 1;
    }

    private static void highlightPlayer(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600));
    }

    private static String getPlayerName(ServerCommandSource source) {
        return source.getName();
    }

    private static String getCurrentPos(ServerCommandSource source) {
        int[] pos = CommandHereWhereHelper.getPos(source);
        return String.format("%d, %d, %d", pos[0], pos[1], pos[2]);
    }

    private static String getOtherPos(ServerCommandSource source) {
        DimensionWrapper dimension = DimensionWrapper.of(source.getWorld());
        int[] pos = CommandHereWhereHelper.getPos(source);
        String otherPos = null;

        if (dimension.getValue() == World.NETHER) {
            otherPos = String.format("%d, %d, %d", pos[0] * 8, pos[1], pos[2] * 8);
        } else if (dimension.getValue() == World.OVERWORLD) {
            otherPos = String.format("%d, %d, %d", pos[0] / 8, pos[1], pos[2] / 8);
        }

        return otherPos;
    }

    @SuppressWarnings("DuplicatedCode")
    private static BaseText message(ServerCommandSource source) {

        DimensionWrapper dimension = DimensionWrapper.of(source.getWorld());
        String playerName = getPlayerName(source);
        String currentPos = getCurrentPos(source);
        String otherPos = getOtherPos(source);
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
                .append(copyButton(currentPos, Layout.DARK_RED)).append(copyButton(otherPos, Layout.GREEN)).append(InvokeFuzzModCommand.highlightCoordButton(currentPos));
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

        return Messenger.f((BaseText) Messenger.s(" [C]").setStyle(
            Messenger.simpleCopyButtonStyle(copyCoordText, Objects.requireNonNull(hoverText), Layout.YELLOW)), Layout.BOLD, buttonColor
        );
    }
}
