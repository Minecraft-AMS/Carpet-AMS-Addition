/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package club.mcams.carpet.mixin.rule.largeBundle;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.helpers.rule.largeBundle.LargeBundleInventory;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("EnhancedSwitchMigration")
@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
    @WrapMethod(method = "use")
    private InteractionResult onUse(Level world, Player user, InteractionHand hand, Operation<InteractionResult> original) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            ItemStack stack = user.getItemInHand(hand);
            SimpleMenuProvider screenHandlerFactory = new SimpleMenuProvider(
                (syncId, playerInv, player) -> {
                    LargeBundleInventory bundleInv = new LargeBundleInventory(stack);
                    switch (AmsServerSettings.largeBundle) {
                        case "9x3":
                            return ChestMenu.threeRows(syncId, playerInv, bundleInv);
                        case "9x6":
                            return ChestMenu.sixRows(syncId, playerInv, bundleInv);
                    }
                    return ChestMenu.sixRows(syncId, playerInv, bundleInv);
                },
                stack.getHoverName()
            );
            world.playSound(user, user.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.5F, 1.35F);
            user.openMenu(screenHandlerFactory);
            return InteractionResult.SUCCESS;
        } else {
            return original.call(world, user, hand);
        }
    }

    @Inject(method = "isBarVisible", at = @At("HEAD"), cancellable = true)
    private void onIsItemBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBarWidth", at = @At("HEAD"), cancellable = true)
    private void onGetItemBarStep(CallbackInfoReturnable<ItemStack> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    private void onGetTooltipData(CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "dropContent(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z", at = @At("HEAD"), cancellable = true)
    private void onDropAllBundledItems(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    private void onClicked(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "overrideStackedOnOther", at = @At("HEAD"), cancellable = true)
    private void onStackClicked(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }
}
