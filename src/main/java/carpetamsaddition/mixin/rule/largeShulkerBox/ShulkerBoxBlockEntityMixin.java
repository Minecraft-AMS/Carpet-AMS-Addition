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

package carpetamsaddition.mixin.rule.largeShulkerBox;

import carpetamsaddition.CarpetAMSAdditionSettings;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.IntStream;


@Mixin(value = ShulkerBoxBlockEntity.class, priority = 1024)
public abstract class ShulkerBoxBlockEntityMixin extends RandomizableContainerBlockEntity implements WorldlyContainer {
    @Unique
    private boolean ams$lastLargeShulkerBox = CarpetAMSAdditionSettings.largeShulkerBox;

    //#if MC<11700
    //$$ protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> blockEntityType) {
    //$$     super(blockEntityType);
    //$$ }
    //#else
    protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    //#endif

    @Shadow
    private NonNullList<@NotNull ItemStack> itemStacks;

    @Shadow
    public abstract int getContainerSize();

    @Inject(
        //#if MC<11700
        //$$ method = "<init>()V",
        //#else
        method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        //#endif
        at = @At("RETURN")
    )
    private void init1(CallbackInfo ci) {
        if (CarpetAMSAdditionSettings.largeShulkerBox) {
            this.itemStacks = NonNullList.withSize(9 * 6, ItemStack.EMPTY);
        }
    }

    @Inject(
        //#if MC<11700
        //$$ method = "<init>(Lnet/minecraft/world/item/DyeColor;)V",
        //#else
        method = "<init>(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        //#endif
        at = @At("RETURN")
    )
    private void init2(CallbackInfo ci) {
        if (CarpetAMSAdditionSettings.largeShulkerBox) {
            this.itemStacks = NonNullList.withSize(9 * 6, ItemStack.EMPTY);
        }
    }

    @Inject(method = "getContainerSize", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if (CarpetAMSAdditionSettings.largeShulkerBox) {
            int size = 9 * 6;
            if (this.itemStacks.size() < size) {
                NonNullList<@NotNull ItemStack> expandedItems = NonNullList.withSize(size, ItemStack.EMPTY);
                for (int slot = 0; slot < this.itemStacks.size(); slot++) {
                    expandedItems.set(slot, this.itemStacks.get(slot));
                }
                this.itemStacks = expandedItems;
            }
            cir.setReturnValue(size);
        } else {
            cir.setReturnValue(9 * 3);
        }
    }

    //#if MC>=11700
    @Inject(method = "tick", at = @At("HEAD"))
    private static void updateComparatorOnRuleChange(Level level, BlockPos pos, BlockState state, ShulkerBoxBlockEntity box, CallbackInfo ci) {
        if (!level.isClientSide()) {
            ((ShulkerBoxBlockEntityMixin) (Object) box).ams$updateRuleState();
        }
    }
    //#else
    //$$ @Inject(method = "tick", at = @At("HEAD"))
    //$$ private void updateComparatorOnRuleChange(CallbackInfo ci) {
    //$$     if (this.level != null && !this.level.isClientSide()) this.ams$updateRuleState();
    //$$ }
    //#endif

    @Unique
    private void ams$updateRuleState() {
        boolean enabled = CarpetAMSAdditionSettings.largeShulkerBox;
        if (this.ams$lastLargeShulkerBox != enabled) {
            this.ams$lastLargeShulkerBox = enabled;
            this.setChanged();
        }
    }

    @Inject(method = "getSlotsForFace", at = @At("HEAD"), cancellable = true)
    private void getAvailableSlots(Direction side, CallbackInfoReturnable<int[]> cir) {
        if (CarpetAMSAdditionSettings.largeShulkerBox) {
            int[] availableSlots = IntStream.range(0, getContainerSize()).toArray();
            cir.setReturnValue(availableSlots);
            cir.cancel();
        }
    }
}
