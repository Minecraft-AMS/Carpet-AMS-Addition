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

package carpetamsaddition.mixin.rule.quickVillagerLevelUp;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.server.level.ServerLevel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagerMixin implements VillagerInvoker {
    @Inject(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;startTrading(Lnet/minecraft/world/entity/player/Player;)V"
        )
    )
    private void quickLevelUp(CallbackInfoReturnable<Integer> cir) {
        if (CarpetAMSAdditionSettings.quickVillagerLevelUp && this.invokerGetVillagerData().level() < 5) {
            Villager villagerEntity = (Villager) (Object) this;
            this.invokerLevelUp((ServerLevel) villagerEntity.level());
        }
    }
}
