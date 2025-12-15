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

package carpetamsaddition.mixin.rule.easyRefreshTrades;

import carpetamsaddition.CarpetAMSAdditionSettings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.entity.npc.villager.VillagerTrades;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagerMixin implements AbstractVillagerInvoker, VillagerInvoker {
    @WrapOperation(
        method = "updateTrades",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;addOffersFromItemListings(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/villager/VillagerTrades$ItemListing;I)V"
        )
    )
    private void refreshRecipes(
        Villager villagerEntity,
        ServerLevel world,
        MerchantOffers tradeOffers,
        VillagerTrades.ItemListing[] factories, int count, Operation<Void> original
    ) {
        if (CarpetAMSAdditionSettings.easyRefreshTrades && isNewMerchant(villagerEntity)) {
            MerchantOffers traderOfferList = villagerEntity.getOffers();
            traderOfferList.clear();
            this.invokeAddOffersFromItemListings(world, traderOfferList, factories, count);
        } else {
            original.call(villagerEntity, world, tradeOffers, factories, count);
        }
    }

    @Inject(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/npc/villager/Villager;startTrading(Lnet/minecraft/world/entity/player/Player;)V"
        )
    )
    private void updateRecipes(Player player, InteractionHand hand, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAMSAdditionSettings.easyRefreshTrades) {
            Villager villagerEntity = (Villager) (Object) this;
            if (isNewMerchant(villagerEntity) && !player.getMainHandItem().getItem().equals(Items.EMERALD_BLOCK)) {
                this.invokeUpdateTrades((ServerLevel) villagerEntity.level());
            }
        }
    }

    @Unique
    private static boolean isNewMerchant(Villager villagerEntity) {
        return villagerEntity.getVillagerXp() <= 0 && villagerEntity.getVillagerData().level() <= 1;
    }
}
