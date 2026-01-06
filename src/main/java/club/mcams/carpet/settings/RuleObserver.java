/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.settings;

import carpet.settings.ParsedRule;
import carpet.settings.Validator;

import net.minecraft.server.command.ServerCommandSource;

public abstract class RuleObserver<T> extends Validator<T> {
    @Override
    public T validate(ServerCommandSource source, ParsedRule<T> rule, T newValue, String userInput) {
        if (rule.get() != newValue) {
            onValueChange(source, rule, rule.get(), newValue);
        }

        return newValue;
    }

    public abstract void onValueChange(ServerCommandSource source, ParsedRule<T> rule, T oldValue, T newValue);

    public String getRuleName(ParsedRule<T> rule) {
        //#if MC>=11900
        //$$ return rule.name();
        //#else
        return rule.name;
        //#endif
    }
}
