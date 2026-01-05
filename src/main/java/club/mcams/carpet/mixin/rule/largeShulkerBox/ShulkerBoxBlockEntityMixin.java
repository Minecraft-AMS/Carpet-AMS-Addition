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

package club.mcams.carpet.mixin.rule.largeShulkerBox;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.AmsServerStaticSettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.IntStream;

@Mixin(value = ShulkerBoxBlockEntity.class, priority = 1024)
public abstract class ShulkerBoxBlockEntityMixin extends LootableContainerBlockEntity implements SidedInventory {
    //#if MC<11700
    //$$ protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> blockEntityType) {
    //$$    super(blockEntityType);
    //$$ }
    //#else
    protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    //#endif

    @Shadow
    private DefaultedList<ItemStack> inventory;

    //#if MC<11700
    //$$ @Inject(method = "<init>()V", at = @At("RETURN"))
    //#else
    @Inject(method = "<init>(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("RETURN"))
    //#endif
    private void init1(CallbackInfo ci) {
        if (!AmsServerSettings.largeShulkerBox) {
            return;
        }

        if (AmsServerStaticSettings.isEnabled(AmsServerStaticSettings.Rule.LARGE_SHULKER_BOX)) {
            this.inventory = DefaultedList.ofSize(9 * 6, ItemStack.EMPTY);
        }
    }

    //#if MC<11700
    //$$ @Inject(method = "<init>(Lnet/minecraft/util/DyeColor;)V", at = @At("RETURN"))
    //#else
    @Inject(method = "<init>(Lnet/minecraft/util/DyeColor;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("RETURN"))
    //#endif
    private void init2(CallbackInfo ci) {
        if (!AmsServerSettings.largeShulkerBox) {
            return;
        }

        if (AmsServerStaticSettings.isEnabled(AmsServerStaticSettings.Rule.LARGE_SHULKER_BOX)) {
            this.inventory = DefaultedList.ofSize(9 * 6, ItemStack.EMPTY);
        }
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if (!AmsServerSettings.largeShulkerBox) {
            return;
        }

        if (AmsServerStaticSettings.isEnabled(AmsServerStaticSettings.Rule.LARGE_SHULKER_BOX)) {
            cir.setReturnValue(9 * 6);
            cir.cancel();
        }
    }

    @Inject(method = "getAvailableSlots", at = @At("HEAD"), cancellable = true)
    private void getAvailableSlots(Direction side, CallbackInfoReturnable<int[]> cir) {
        if (!AmsServerSettings.largeShulkerBox) {
            return;
        }

        if (AmsServerStaticSettings.isEnabled(AmsServerStaticSettings.Rule.LARGE_SHULKER_BOX)) {
            int[] availableSlots = IntStream.range(0, 9 * 6).toArray();
            cir.setReturnValue(availableSlots);
            cir.cancel();
        }
    }
}