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

package club.mcams.carpet.commands.rule.commandAnvilInteractionDisabled;

import club.mcams.carpet.AmsServerSettings;

import club.mcams.carpet.utils.Colors;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.compat.LiteralTextUtil;

import net.minecraft.text.Text;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;

public class AnvilInteractionDisabledCommandRegistry {
    private static final Translator translator = new Translator("command.anvilInteractionDisabled");
    public static boolean anvilInteractionDisabled = false;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("anvilInteractionDisabled")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandAnvilInteractionDisabled))
            .then(argument("mode", BoolArgumentType.bool())
            .executes(context -> {
                boolean mode = BoolArgumentType.getBool(context, "mode");
                anvilInteractionDisabled = mode;
                Text message =
                        mode ?
                        LiteralTextUtil.createColoredText(translator.tr("disable").getString(), Colors.WHITE, true, false) :
                        LiteralTextUtil.createColoredText(translator.tr("enable").getString(), Colors.WHITE, true, false);
                Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(message, true);
                return 1;
        })));
    }
}