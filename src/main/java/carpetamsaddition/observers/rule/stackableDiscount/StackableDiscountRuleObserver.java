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

package carpetamsaddition.observers.rule.stackableDiscount;

import carpet.api.settings.CarpetRule;

import carpetamsaddition.mixin.rule.stackableDiscount.VillageGossipTypeAccessor;
import carpetamsaddition.settings.SimpleRuleObserver;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.ai.gossip.GossipType;

@SuppressWarnings({"unused", "DataFlowIssue"})
public class StackableDiscountRuleObserver extends SimpleRuleObserver<Boolean> {
    @Override
    public void onValueChange(CommandSourceStack source, CarpetRule<Boolean> rule, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            ((VillageGossipTypeAccessor) (Object) GossipType.MINOR_POSITIVE).setMax(200);
            ((VillageGossipTypeAccessor) (Object) GossipType.MAJOR_POSITIVE).setMax(100);
            ((VillageGossipTypeAccessor) (Object) GossipType.MAJOR_POSITIVE).setDecayPerTransfer(100);
        } else {
            ((VillageGossipTypeAccessor) (Object) GossipType.MINOR_POSITIVE).setMax(25);
            ((VillageGossipTypeAccessor) (Object) GossipType.MAJOR_POSITIVE).setMax(20);
            ((VillageGossipTypeAccessor) (Object) GossipType.MAJOR_POSITIVE).setDecayPerTransfer(20);
        }
    }
}
