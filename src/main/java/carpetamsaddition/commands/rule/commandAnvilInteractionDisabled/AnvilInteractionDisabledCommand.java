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
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.config.rule.amsUpdateSuppressionCrashFix.ForceModeCommandConfig;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messenger.Messenger;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.commands.CommandSourceStack;

public class AnvilInteractionDisabledCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.anvilInteractionDisabled");
    public static boolean anvilInteractionDisabled = false;

    @Override
    public void define(CommandBuilder command) {
        command.name("anvilInteractionDisabled")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandAnvilInteractionDisabled)
            .route(Arguments.bool("mode"))
            .executes(c -> setMode(c.source(), c.get(Arguments.bool("mode"))));
    }

    private static int setMode(CommandSourceStack source, boolean mode) {
        anvilInteractionDisabled = mode;
        MutableComponent message =
            anvilInteractionDisabled ?
            Messenger.f(tr.tr("disable"), Layout.LIGHT_PURPLE) :
            Messenger.f(tr.tr("enable"), Layout.GREEN);
        Messenger.tell(source, message, true);
        ForceModeCommandConfig.saveConfigToJson(source.getServer());
        return 1;
    }
}
