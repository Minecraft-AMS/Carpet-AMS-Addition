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

package carpetamsaddition.commands.rule.commandGetHeldItemID;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import java.util.Objects;

public class GetHeldItemIDCommandRegistry {
    private static final Translator tr = new Translator("command.commandGetHeldItemID");
    private static final String MSG_HEAD = "<commandGetHeldItemID> ";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("getHeldItemID")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandGetHeldItemID))
            .executes(context -> execute(context.getSource()))
        );
    }

    private static int execute(CommandSourceStack source) {
        String mainHandItemID = getHeldItemRegisterName(Objects.requireNonNull(source.getPlayer()));
        MutableComponent message = buildMessage(mainHandItemID);
        Messenger.tell(source, message);
        return 1;
    }

    private static MutableComponent buildMessage(String itemID) {
        return
            Messenger.c(
                Messenger.f(Messenger.s(MSG_HEAD), ChatFormatting.AQUA),
                Messenger.f(Messenger.s(itemID), ChatFormatting.GREEN)
            ).append(createCopyButton(itemID));
    }

    private static Component createCopyButton(String itemID) {
        return
            Messenger.f(Messenger.s(" [C] ").setStyle(
                Messenger.simpleCopyButtonStyle(itemID, tr.tr("getHeldItemID.copy"), ChatFormatting.YELLOW)
            ), ChatFormatting.GREEN, ChatFormatting.BOLD);
    }

    private static String getHeldItemRegisterName(Player player) {
        return RegexTools.getItemRegisterName(player.getMainHandItem());
    }
}
