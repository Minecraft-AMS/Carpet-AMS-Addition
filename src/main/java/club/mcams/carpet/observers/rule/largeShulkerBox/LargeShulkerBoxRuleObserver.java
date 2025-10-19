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

package club.mcams.carpet.observers.rule.largeShulkerBox;

import carpet.settings.ParsedRule;

import club.mcams.carpet.mixin.rule.largeShulkerBox.ShulkerBoxBlockEntityAccessor;
import club.mcams.carpet.settings.SimpleRuleObserver;

import net.minecraft.server.command.ServerCommandSource;

public class LargeShulkerBoxRuleObserver extends SimpleRuleObserver<Boolean> {
    @Override
    public void onValueChange(ServerCommandSource source, ParsedRule<Boolean> rule, Boolean oldValue, Boolean newValue) {
        //#if MC>=11700
        if (newValue) {
            ShulkerBoxBlockEntityAccessor.setInventorySize(54);
        } else {
            ShulkerBoxBlockEntityAccessor.setInventorySize(27);
        }
        //#endif
    }
}
