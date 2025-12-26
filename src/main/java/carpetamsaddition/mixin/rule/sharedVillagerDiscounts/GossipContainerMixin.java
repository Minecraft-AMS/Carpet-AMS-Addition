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

package carpetamsaddition.mixin.rule.sharedVillagerDiscounts;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.gossip.GossipContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.Predicate;

@Mixin(GossipContainer.class)
public abstract class GossipContainerMixin implements GossipContainerAccessor, EntityGossipsInvoker {
    @Inject(method = "getReputation(Ljava/util/UUID;Ljava/util/function/Predicate;)I", at = @At("HEAD"), cancellable = true)
    private void getReputation(UUID target, Predicate<GossipType> filter, CallbackInfoReturnable<Integer> cir) {
        if (CarpetAMSAdditionSettings.sharedVillagerDiscounts && filter.test(GossipType.MAJOR_POSITIVE)) {
            EntityGossipsInvoker targetReputation = (EntityGossipsInvoker) this.getGossips().get(target);
            int otherRep = 0;

            if (targetReputation != null) {
                otherRep = targetReputation.invokeWeightedValue(vgt -> filter.test(vgt) && !vgt.equals(GossipType.MAJOR_POSITIVE));
            }

            int majorPositiveRep = 0;

            for (Object reputation : this.getGossips().values()) {
                EntityGossipsInvoker invoker = (EntityGossipsInvoker) reputation;
                majorPositiveRep += invoker.invokeWeightedValue(vgt -> vgt.equals(GossipType.MAJOR_POSITIVE));
            }

            int maxMajorPositiveRep = GossipType.MAJOR_POSITIVE.max * GossipType.MAJOR_POSITIVE.weight;

            cir.setReturnValue(otherRep + Math.min(majorPositiveRep, maxMajorPositiveRep));
        }
    }
}
