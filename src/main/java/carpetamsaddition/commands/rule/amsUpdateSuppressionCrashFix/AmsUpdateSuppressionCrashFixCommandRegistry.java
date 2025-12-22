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

package carpetamsaddition.commands.rule.amsUpdateSuppressionCrashFix;

import carpetamsaddition.config.rule.amsUpdateSuppressionCrashFix.ForceModeCommandConfig;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Colors;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Messenger;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AmsUpdateSuppressionCrashFixCommandRegistry {
    private static final Translator tr = new Translator("command.amsUpdateSuppressionCrashFixForceMode");
    public static boolean amsUpdateSuppressionCrashFixForceMode = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("amsUpdateSuppressionCrashFixForceMode")
        .requires(source -> CommandHelper.canUseCommand(source, 2))
        .then(argument("mode", BoolArgumentType.bool())
        .executes(context -> setMode(context, context.getSource()))));
    }

    private static int setMode(CommandContext<CommandSourceStack> context, CommandSourceStack source) {
        amsUpdateSuppressionCrashFixForceMode = BoolArgumentType.getBool(context, "mode");
        MutableComponent message =
            amsUpdateSuppressionCrashFixForceMode ?
            tr.tr("force_mode").withColor(Colors.LIGHT_PURPLE) :
            tr.tr("lazy_mode").withColor(Colors.GREEN);
        Messenger.tell(source, message);
        ForceModeCommandConfig.saveConfigToJson(context.getSource().getServer());
        return 1;
    }
}
