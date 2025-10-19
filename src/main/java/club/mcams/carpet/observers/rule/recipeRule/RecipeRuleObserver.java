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

package club.mcams.carpet.observers.rule.recipeRule;

import carpet.settings.ParsedRule;

import club.mcams.carpet.helpers.rule.recipeRule.RecipeRuleHelper;
import club.mcams.carpet.settings.SimpleRuleObserver;
import club.mcams.carpet.utils.MinecraftServerUtil;

import net.minecraft.server.command.ServerCommandSource;

public class RecipeRuleObserver extends SimpleRuleObserver<Boolean> {
    @Override
    public void onValueChange(ServerCommandSource source, ParsedRule<Boolean> rule, Boolean oldValue, Boolean newValue) {
        RecipeRuleHelper.onValueChange(MinecraftServerUtil.getServer());
    }
}
