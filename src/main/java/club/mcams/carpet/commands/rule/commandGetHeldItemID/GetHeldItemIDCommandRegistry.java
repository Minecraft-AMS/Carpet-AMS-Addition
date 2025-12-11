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

package club.mcams.carpet.commands.rule.commandGetHeldItemID;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class GetHeldItemIDCommandRegistry {
    private static final Translator translator = new Translator("command.commandGetHeldItemID");
    private static final String MSG_HEAD = "<commandGetHeldItemID> ";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("getHeldItemID")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetHeldItemID))
            .executes(context -> execute(context.getSource().getPlayerOrException()))
        );
    }

    private static int execute(Player player) {
        String mainHandItemID = getHeldItemRegisterName(player);
        Component message = buildMessage(mainHandItemID);
        player.displayClientMessage(message, false);
        return 1;
    }

    private static Component buildMessage(String itemID) {
        return
            Messenger.s("")
            .append(Messenger.s(MSG_HEAD).withStyle(ChatFormatting.AQUA))
            .append(Messenger.s(itemID).withStyle(ChatFormatting.GREEN))
            .append(createCopyButton(itemID));
    }

    private static Component createCopyButton(String itemID) {
        return Messenger.s(" [C] ").setStyle(
            Style.EMPTY.withColor(ChatFormatting.GREEN).withBold(true)
            .withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, itemID))
            .withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, getCopyHoverText()))
        );
    }

    private static Component getCopyHoverText() {
        return Messenger.s(translator.tr("getHeldItemID.copy")).withStyle(ChatFormatting.YELLOW);
    }

    private static String getHeldItemRegisterName(Player player) {
        return RegexTools.getItemRegisterName(player.getMainHandItem().getItem().toString());
    }
}
