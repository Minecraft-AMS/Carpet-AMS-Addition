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
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messenger.Messenger;
import carpetamsaddition.utils.RegexTools;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class GetHeldItemIDCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.commandGetHeldItemID");
    private static final String MSG_HEAD = "<commandGetHeldItemID> ";

    @Override
    public void define(CommandBuilder command) {
        command.name("getHeldItemID")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandGetHeldItemID)
            .executes(c -> execute(c.source()));
    }

    private static int execute(CommandSourceStack source) throws CommandSyntaxException {
        String mainHandItemID = getHeldItemRegisterName(Objects.requireNonNull(source.getPlayer()));
        Component message = buildMessage(mainHandItemID);
        Messenger.tell(source, (MutableComponent) message);
        return 1;
    }

    private static Component buildMessage(String itemID) {
        return
            Messenger.c(
                Messenger.f(Messenger.s(MSG_HEAD), Layout.AQUA),
                Messenger.f(Messenger.s(itemID), Layout.GREEN)
            ).append(createCopyButton(itemID));
    }

    private static Component createCopyButton(String itemID) {
        return
            Messenger.f(Messenger.style(
                Messenger.s(" [C] "),
                Messenger.simpleCopyButtonStyle(itemID, tr.tr("getHeldItemID.copy"), Layout.YELLOW)
            ), Layout.GREEN, Layout.BOLD);
    }

    private static String getHeldItemRegisterName(Player player) {
        return RegexTools.getItemRegisterName(player.getMainHandItem());
    }
}
