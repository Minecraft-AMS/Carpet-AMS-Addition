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

package club.mcams.carpet.mixin.rule.easyRefreshTrades;

import club.mcams.carpet.AmsServerSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin implements AbstractTraderEntityInvoker, VillagerEntityInvoker {
    @WrapOperation(
        method = "fillRecipes",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/VillagerEntity;fillRecipesFromPool(Lnet/minecraft/village/TradeOfferList;[Lnet/minecraft/village/TradeOffers$Factory;I)V"
        )
    )
    private void refreshRecipes(VillagerEntity villagerEntity, TradeOfferList tradeOffers, TradeOffers.Factory[] factories, int count, Operation<Void> original) {
        if (AmsServerSettings.easyRefreshTrades && isNewMerchant(villagerEntity)) {
            TradeOfferList traderOfferList = villagerEntity.getOffers();
            traderOfferList.clear();
            this.invokeFillRecipesFromPool(traderOfferList, factories, count);
        } else {
            original.call(villagerEntity, tradeOffers, factories, count);
        }
    }

    @Inject(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/VillagerEntity;beginTradeWith(Lnet/minecraft/entity/player/PlayerEntity;)V"
        )
    )
    private void updateRecipes(PlayerEntity player, Hand hand, CallbackInfoReturnable<Boolean> cir) {
        if (AmsServerSettings.easyRefreshTrades) {
            VillagerEntity villagerEntity = (VillagerEntity) (Object) this;
            if (isNewMerchant(villagerEntity)) {
                this.invokeFillRecipes();
            }
        }
    }

    @Unique
    private static boolean isNewMerchant(VillagerEntity villagerEntity) {
        return villagerEntity.getExperience() <= 0 && villagerEntity.getVillagerData().getLevel() <= 1;
    }
}
