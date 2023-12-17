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

import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import static net.minecraft.village.VillageGossipType.MAJOR_POSITIVE;

@Mixin(VillagerGossips.class)
public abstract class VillagerGossipsMixin {

    @Shadow
    @Final
    private Map<UUID, Object> entityReputation;

    @Inject(method = "getReputationFor(Ljava/util/UUID;Ljava/util/function/Predicate;)I", at = @At("HEAD"), cancellable = true)
    private void getReputation(UUID target, Predicate<VillageGossipType> filter, CallbackInfoReturnable<Integer> cir) {
        if (AmsServerSettings.sharedVillagerDiscounts && filter.test(MAJOR_POSITIVE)) {
            GetValueForInvoker targetReputation = (GetValueForInvoker)entityReputation.get(target);
            int otherRepertory = 0;
            if (targetReputation != null) {
                otherRepertory = targetReputation._getValueFor(vgt -> filter.test(vgt) && !vgt.equals(MAJOR_POSITIVE));
            }
            int majorPositiveRepertory = entityReputation.values().stream().mapToInt(r -> ((GetValueForInvoker) r)._getValueFor(vgt -> vgt.equals(MAJOR_POSITIVE))).sum();
            cir.setReturnValue(otherRepertory + Math.min(majorPositiveRepertory, MAJOR_POSITIVE.maxValue * MAJOR_POSITIVE.multiplier));
        }
    }
}
