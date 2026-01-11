/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.observers;

import carpet.api.settings.CarpetRule;

import carpetamsaddition.helpers.EnvironmentHelper;
import carpetamsaddition.settings.RuleObserver;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CarpetUtil;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.MinecraftServerUtil;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;

public class NeedRestartServerOrClientObserver extends RuleObserver<Object> {
    private static final Translator tr = new Translator("observer.need_restart_server_or_client");

    @Override
    public void onValueChange(CommandSourceStack source, CarpetRule<Object> rule, Object oldValue, Object newValue) {
        MutableComponent message = null;

        if (EnvironmentHelper.isClient() && newValue != oldValue) {
            message = tr.tr("is_client_message", CarpetUtil.getRuleName(rule));
        } else if (EnvironmentHelper.isServer()) {
            message = tr.tr("is_server_message", CarpetUtil.getRuleName(rule));
        }

        if (message != null && source != null && MinecraftServerUtil.serverIsRunning()) {
            Messenger.tell(source, Messenger.f(message, Layout.YELLOW));
        }
    }
}
