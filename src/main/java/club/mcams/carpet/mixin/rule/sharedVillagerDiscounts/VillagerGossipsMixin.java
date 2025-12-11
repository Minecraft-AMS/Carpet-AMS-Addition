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

package club.mcams.carpet.mixin.rule.sharedVillagerDiscounts;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.village.VillagerGossipType;
import net.minecraft.village.VillagerGossips;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.Predicate;

@Mixin(VillagerGossips.class)
public abstract class VillagerGossipsMixin implements VillagerGossipsAccessor, VillagerGossips_ReputationInvoker {
    @Inject(method = "getReputationFor(Ljava/util/UUID;Ljava/util/function/Predicate;)I", at = @At("HEAD"), cancellable = true)
    private void getReputation(UUID target, Predicate<VillagerGossipType> filter, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.sharedVillagerDiscounts && filter.test(VillagerGossipType.MAJOR_POSITIVE)) {
            VillagerGossips_ReputationInvoker targetReputation = (VillagerGossips_ReputationInvoker) this.getEntityReputation().get(target);
            int otherRep = 0;
            if (targetReputation != null) {
                otherRep = targetReputation.invokeGetValueFor(vgt -> filter.test(vgt) && !vgt.equals(VillagerGossipType.MAJOR_POSITIVE));
            }
            int majorPositiveRep = 0;
            for (Object reputation : this.getEntityReputation().values()) {
                VillagerGossips_ReputationInvoker invoker = (VillagerGossips_ReputationInvoker) reputation;
                majorPositiveRep += invoker.invokeGetValueFor(vgt -> vgt.equals(VillagerGossipType.MAJOR_POSITIVE));
            }
            int maxMajorPositiveRep = VillagerGossipType.MAJOR_POSITIVE.maxValue * VillagerGossipType.MAJOR_POSITIVE.multiplier;
            cir.setReturnValue(otherRep + Math.min(majorPositiveRep, maxMajorPositiveRep));
        }
    }
}
