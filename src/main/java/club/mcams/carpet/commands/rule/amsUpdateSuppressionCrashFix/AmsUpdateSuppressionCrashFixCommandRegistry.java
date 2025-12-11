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
import club.mcams.carpet.utils.Messenger;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.ChatFormatting;

import java.util.Objects;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class AmsUpdateSuppressionCrashFixCommandRegistry {
    private static final Translator translator = new Translator("command.amsUpdateSuppressionCrashFixForceMode");
    public static boolean amsUpdateSuppressionCrashFixForceMode = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("amsUpdateSuppressionCrashFixForceMode")
        .requires(source -> CommandHelper.canUseCommand(source, 2))
        .then(argument("mode", BoolArgumentType.bool())
        .executes(context -> {
            boolean mode = BoolArgumentType.getBool(context, "mode");
            amsUpdateSuppressionCrashFixForceMode = mode;
            ForceModeCommandConfig.saveConfigToJson(context.getSource().getServer());
            Component message =
                    mode ?
                    Messenger.s(translator.tr("force_mod").getString()).setStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)) :
                    Messenger.s(translator.tr("lazy_mod").getString()).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
            Objects.requireNonNull(context.getSource().getPlayerOrException()).displayClientMessage(message, true);
            return 1;
        })));
    }
}