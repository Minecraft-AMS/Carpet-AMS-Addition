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

package club.mcams.carpet.observers.rule;

import carpet.settings.ParsedRule;

import club.mcams.carpet.helpers.EnvironmentHelper;
import club.mcams.carpet.settings.RuleObserver;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.MinecraftServerUtil;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.21.2")
@SuppressWarnings("unused")
public class NeedRestartServerOrClientObserver extends RuleObserver<Object> {
    private static final Translator tr = new Translator("validator.need_restart_server_or_client");

    @Override
    public void onValueChange(ServerCommandSource source, ParsedRule<Object> rule, Object oldValue, Object newValue) {
        BaseText message = null;

        if (EnvironmentHelper.isClient() && newValue != oldValue) {
            message = tr.tr("is_client_message", this.getRuleName(rule));
        } else if (EnvironmentHelper.isServer()) {
            message = tr.tr("is_server_message", this.getRuleName(rule));
        }

        if (message != null && source != null && MinecraftServerUtil.serverIsRunning()) {
            Messenger.tell(source, Messenger.f(message, Layout.YELLOW));
        }
    }
}
