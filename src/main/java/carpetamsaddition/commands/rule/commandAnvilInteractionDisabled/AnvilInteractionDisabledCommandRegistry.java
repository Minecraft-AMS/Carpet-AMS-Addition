/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
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

package carpetamsaddition.commands.rule.commandAnvilInteractionDisabled;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Messenger;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.ChatFormatting;

import static net.minecraft.commands.Commands.argument;

import java.util.Objects;

public class AnvilInteractionDisabledCommandRegistry {
    private static final Translator translator = new Translator("command.anvilInteractionDisabled");
    public static boolean anvilInteractionDisabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("anvilInteractionDisabled")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandAnvilInteractionDisabled))
            .then(argument("mode", BoolArgumentType.bool())
            .executes(context -> {
                boolean mode = BoolArgumentType.getBool(context, "mode");
                anvilInteractionDisabled = mode;
                Component message =
                        mode ?
                        Messenger.s(translator.tr("disable").getString()).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)) :
                        Messenger.s(translator.tr("enable").getString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
                Objects.requireNonNull(context.getSource().getPlayerOrException()).displayClientMessage(message, true);
                return 1;
        })));
    }
}