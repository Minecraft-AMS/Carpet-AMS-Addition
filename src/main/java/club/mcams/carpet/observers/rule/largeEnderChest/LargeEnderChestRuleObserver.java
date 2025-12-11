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

package club.mcams.carpet.observers.rule.largeEnderChest;

import carpet.api.settings.CarpetRule;

import club.mcams.carpet.settings.SimpleRuleObserver;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.MinecraftServerUtil;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class LargeEnderChestRuleObserver extends SimpleRuleObserver<Boolean> {
    private static final Translator translator = new Translator("validator.largeEnderChest");
    private static final String MSG_HEAD = "<Carpet AMS Addition> ";

    @Override
    public void onValueChange(ServerCommandSource source, CarpetRule<Boolean> rule, Boolean oldValue, Boolean newValue) {
        if (newValue && MinecraftServerUtil.serverIsRunning()) {
            Messenger.sendServerMessage(MinecraftServerUtil.getServer(), message());
        }
    }

    private static MutableText message() {
        return Messenger.s(MSG_HEAD + translator.tr("switch_tip").getString()).formatted(Formatting.GREEN);
    }
}
