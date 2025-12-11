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

import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

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
    private ActionResult onUse(World world, PlayerEntity user, Hand hand, Operation<ActionResult> original) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            ItemStack stack = user.getStackInHand(hand);
            SimpleNamedScreenHandlerFactory screenHandlerFactory = new SimpleNamedScreenHandlerFactory(
                (syncId, playerInv, player) -> {
                    LargeBundleInventory bundleInv = new LargeBundleInventory(stack);
                    switch (AmsServerSettings.largeBundle) {
                        case "9x3":
                            return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInv, bundleInv);
                        case "9x6":
                            return GenericContainerScreenHandler.createGeneric9x6(syncId, playerInv, bundleInv);
                    }
                    return GenericContainerScreenHandler.createGeneric9x6(syncId, playerInv, bundleInv);
                },
                stack.getName()
            );
            world.playSound(user, user.getBlockPos(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.PLAYERS, 1.5F, 1.35F);
            user.openHandledScreen(screenHandlerFactory);
            return ActionResult.SUCCESS;
        } else {
            return original.call(world, user, hand);
        }
    }

    @Inject(method = "isItemBarVisible", at = @At("HEAD"), cancellable = true)
    private void onIsItemBarVisible(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getItemBarStep", at = @At("HEAD"), cancellable = true)
    private void onGetItemBarStep(CallbackInfoReturnable<ItemStack> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(method = "getTooltipData", at = @At("HEAD"), cancellable = true)
    private void onGetTooltipData(CallbackInfoReturnable<Optional<TooltipData>> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "dropFirstBundledStack", at = @At("HEAD"), cancellable = true)
    private void onDropAllBundledItems(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
    private void onClicked(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onStackClicked", at = @At("HEAD"), cancellable = true)
    private void onStackClicked(CallbackInfoReturnable<Boolean> cir) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false")) {
            cir.setReturnValue(false);
        }
    }
}
