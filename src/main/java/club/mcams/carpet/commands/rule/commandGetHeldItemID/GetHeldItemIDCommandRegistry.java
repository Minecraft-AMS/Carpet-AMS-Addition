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
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.RegexTools;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.MutableText;

import java.util.Objects;

public class GetHeldItemIDCommandRegistry {
    private static final Translator tr = new Translator("command.commandGetHeldItemID");
    private static final String MSG_HEAD = "<commandGetHeldItemID> ";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("getHeldItemID")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetHeldItemID))
            .executes(context -> execute(context.getSource()))
        );
    }

    private static int execute(ServerCommandSource source) throws CommandSyntaxException {
        String mainHandItemID = getHeldItemRegisterName(Objects.requireNonNull(source.getPlayer()));
        MutableText message = buildMessage(mainHandItemID);
        Messenger.tell(source, (BaseText) message);
        return 1;
    }

    private static MutableText buildMessage(String itemID) {
        return
            Messenger.c(
                Messenger.f(Messenger.s(MSG_HEAD), Layout.AQUA),
                Messenger.f(Messenger.s(itemID), Layout.GREEN)
            ).append(createCopyButton(itemID));
    }

    private static BaseText createCopyButton(String itemID) {
        return
            Messenger.f((BaseText) Messenger.s(" [C] ").setStyle(
                Messenger.simpleCopyButtonStyle(itemID, tr.tr("getHeldItemID.copy"), Layout.YELLOW)
            ), Layout.GREEN, Layout.BOLD);
    }

    private static String getHeldItemRegisterName(PlayerEntity player) {
        return RegexTools.getItemRegisterName(player.getMainHandStack());
    }
}
