/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package carpetamsaddition.observers.network;

import carpet.api.settings.CarpetRule;

import carpetamsaddition.utils.Messenger;
import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.settings.SimpleRuleObserver;
import carpetamsaddition.utils.MinecraftServerUtil;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

public class NetworkProtocolObserver extends SimpleRuleObserver<Object> {
    private final Translator tr = new Translator("validator.amsNetworkProtocol");

    @Override
    public void onValueChange(CommandSourceStack source, CarpetRule<Object> rule, Object oldValue, Object newValue) {
        if (!AmsServerSettings.amsNetworkProtocol && MinecraftServerUtil.serverIsRunning()) {
            Messenger.tell(source, Messenger.formatting(tr.tr("need_enable_protocol", getRuleName(rule)), ChatFormatting.YELLOW));
        }
    }
}
