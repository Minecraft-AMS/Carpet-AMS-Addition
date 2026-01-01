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
    protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Unique
    private static final boolean ENABLE_FLAG$AMS;

    static {
        ENABLE_FLAG$AMS = CarpetAMSAdditionSettings.largeShulkerBox;
    }

    @Shadow
    private NonNullList<@NotNull ItemStack> itemStacks;

    @Shadow
    public abstract int getContainerSize();

    @Inject(method = "<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("RETURN"))
    private void init1(CallbackInfo ci) {
        if (ENABLE_FLAG$AMS) {
            this.itemStacks = NonNullList.withSize(9 * 6, ItemStack.EMPTY);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", at = @At("RETURN"))
    private void init2(CallbackInfo ci) {
        if (ENABLE_FLAG$AMS) {
            this.itemStacks = NonNullList.withSize(9 * 6, ItemStack.EMPTY);
        }
    }

    @Inject(method = "getContainerSize", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if (ENABLE_FLAG$AMS) {
            cir.setReturnValue(9 * 6);
            cir.cancel();
        }
    }

    @Inject(method = "getSlotsForFace", at = @At("HEAD"), cancellable = true)
    private void getAvailableSlots(Direction side, CallbackInfoReturnable<int[]> cir) {
        if (ENABLE_FLAG$AMS) {
            int[] availableSlots = IntStream.range(0, getContainerSize()).toArray();
            cir.setReturnValue(availableSlots);
            cir.cancel();
        }
    }
}