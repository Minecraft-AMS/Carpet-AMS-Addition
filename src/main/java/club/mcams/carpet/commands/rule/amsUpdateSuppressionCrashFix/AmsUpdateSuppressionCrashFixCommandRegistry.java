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

package club.mcams.carpet.commands.rule.amsUpdateSuppressionCrashFix;

import club.mcams.carpet.config.rule.amsUpdateSuppressionCrashFix.ForceModeCommandConfig;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.text.BaseText;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AmsUpdateSuppressionCrashFixCommandRegistry {
    private static final Translator tr = new Translator("command.amsUpdateSuppressionCrashFixForceMode");
    public static boolean amsUpdateSuppressionCrashFixForceMode = false;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("amsUpdateSuppressionCrashFixForceMode")
        .requires(source -> CommandHelper.canUseCommand(source, 2))
        .then(argument("mode", BoolArgumentType.bool())
        .executes(context -> setMode(context, context.getSource()))));
    }

    private static int setMode(CommandContext<ServerCommandSource> context, ServerCommandSource source) {
        amsUpdateSuppressionCrashFixForceMode = BoolArgumentType.getBool(context, "mode");
        BaseText message =
            amsUpdateSuppressionCrashFixForceMode ?
            Messenger.f(tr.tr("force_mode"), Layout.LIGHT_PURPLE) :
            Messenger.f(tr.tr("lazy_mode"), Layout.GREEN);
        Messenger.tell(source, message);
        ForceModeCommandConfig.saveConfigToJson(context.getSource().getServer());
        return 1;
    }
}
