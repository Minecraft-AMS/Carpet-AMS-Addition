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

package club.mcams.carpet.observers.rule.stackableDiscount;

import carpet.settings.ParsedRule;

import club.mcams.carpet.mixin.rule.stackableDiscount.VillageGossipTypeAccessor;
import club.mcams.carpet.settings.RuleObserver;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.village.VillageGossipType;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft >= 1.20.2")
@SuppressWarnings({"unused", "DataFlowIssue"})
public class StackableDiscountRuleObserver extends RuleObserver<Boolean> {
    @Override
    public void onValueChange(ServerCommandSource source, ParsedRule<Boolean> rule, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            ((VillageGossipTypeAccessor) (Object) VillageGossipType.MINOR_POSITIVE).setMaxValue(200);
            ((VillageGossipTypeAccessor) (Object) VillageGossipType.MAJOR_POSITIVE).setMaxValue(100);
            ((VillageGossipTypeAccessor) (Object) VillageGossipType.MAJOR_POSITIVE).setShareDecrement(100);
        } else {
            ((VillageGossipTypeAccessor) (Object) VillageGossipType.MINOR_POSITIVE).setMaxValue(25);
            ((VillageGossipTypeAccessor) (Object) VillageGossipType.MAJOR_POSITIVE).setMaxValue(20);
            ((VillageGossipTypeAccessor) (Object) VillageGossipType.MAJOR_POSITIVE).setShareDecrement(20);
        }
    }
}
