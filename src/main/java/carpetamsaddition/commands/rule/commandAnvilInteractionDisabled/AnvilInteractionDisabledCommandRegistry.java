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
import carpetamsaddition.config.rule.amsUpdateSuppressionCrashFix.ForceModeCommandConfig;
import carpetamsaddition.utils.Colors;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Messenger;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AnvilInteractionDisabledCommandRegistry {
    private static final Translator tr = new Translator("command.anvilInteractionDisabled");
    public static boolean anvilInteractionDisabled = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            literal("anvilInteractionDisabled")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandAnvilInteractionDisabled))
            .then(argument("mode", BoolArgumentType.bool())
            .executes(c -> setMode(c, c.getSource())
        )));
    }

    private static int setMode(CommandContext<CommandSourceStack> context, CommandSourceStack source) {
        anvilInteractionDisabled = BoolArgumentType.getBool(context, "mode");
        MutableComponent message =
            anvilInteractionDisabled ?
            tr.tr("disable").withColor(Colors.LIGHT_PURPLE) :
            tr.tr("enable").withColor(Colors.GREEN);
        Messenger.tell(source, message, true);
        ForceModeCommandConfig.saveConfigToJson(context.getSource().getServer());
        return 1;
    }
}
