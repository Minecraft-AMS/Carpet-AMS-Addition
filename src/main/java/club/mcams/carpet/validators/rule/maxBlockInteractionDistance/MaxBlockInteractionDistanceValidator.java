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

package club.mcams.carpet.validators.rule.maxBlockInteractionDistance;

import carpet.settings.ParsedRule;
import carpet.settings.Validator;

import net.minecraft.server.command.ServerCommandSource;

public class MaxBlockInteractionDistanceValidator extends Validator<Double> {
    @Override
    public Double validate(ServerCommandSource serverCommandSource, ParsedRule<Double> parsedRule, Double aDouble, String s) {
        if ((aDouble >= 0.0D && aDouble <= 512.0D) || aDouble == -1.0D) {
            return aDouble;
        } else {
            return null;
        }
    }

    @Override
    public String description() {
        return "The value must be 0 - 512 or -1";
    }
}
