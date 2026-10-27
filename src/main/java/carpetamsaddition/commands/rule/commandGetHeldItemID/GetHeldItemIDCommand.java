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

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

 public class GetHeldItemIDCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.commandGetHeldItemID");

    @Override
    public void define(CommandBuilder command) {
        command.name("getHeldItemID")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandGetHeldItemID)
            .executes(c -> execute(c.source()));
    }

    private static int execute(CommandSourceStack source) throws CommandSyntaxException {
        String mainHandItemID = getHeldItemRegisterName(source.getPlayerOrException());
        Component message = buildMessage(mainHandItemID);
        Messenger.tell(source, message);
        return 1;
    }

    private static Component buildMessage(String itemID) {
        return
            Messenger.c(
                Messenger.f(tr.tr("msg_head"), Layout.AQUA),
                Messenger.f(Messenger.s(itemID), Layout.GREEN)
            )
            .append(Messenger.copyButton(itemID));
    }

    private static String getHeldItemRegisterName(Player player) {
        return BuiltInRegistries.ITEM.getKey(player.getMainHandItem().getItem()).toString();
    }
}
