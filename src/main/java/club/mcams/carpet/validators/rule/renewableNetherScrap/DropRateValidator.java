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

package club.mcams.carpet.validators.rule.renewableNetherScrap;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;

import club.mcams.carpet.translations.Translator;

import net.minecraft.server.command.ServerCommandSource;

public class DropRateValidator extends Validator<Double> {
    private static final Translator translator = new Translator("validator.renewableNetherScrap");

    @Override
    public Double validate(ServerCommandSource serverCommandSource, CarpetRule<Double> parsedRule, Double aDouble, String s) {
        return aDouble >= 0.0D && aDouble <= 1.0D ? aDouble : null;
    }

    @Override
    public String description() {
        return translator.tr("value_range").getString();
    }
}
