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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

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

    @Unique
    private int ams$sizeBeforeExpansion = -1;

    protected ShulkerBoxBlockEntityMixin(
        BlockEntityType<?> blockEntityType
        //#if MC>=11700
        , BlockPos blockPos, BlockState blockState
        //#endif
    ) {
        super(
            blockEntityType
            //#if MC>=11700
            , blockPos, blockState
            //#endif
        );
    }

    @Shadow
    private NonNullList<@NotNull ItemStack> itemStacks;

    @Shadow
    private int openCount;

    @Shadow
    public abstract int getContainerSize();

    @ModifyReturnValue(method = "getContainerSize", at = @At("RETURN"))
    private int size(int original) {
        int currentSize = this.itemStacks.size();
        if (CarpetAMSAdditionSettings.largeShulkerBox) {
            int size = 9 * 6;

            if (currentSize < size) {
                NonNullList<@NotNull ItemStack> expandedItems = NonNullList.withSize(size, ItemStack.EMPTY);

                for (int slot = 0; slot < currentSize; slot++) {
                    expandedItems.set(slot, this.itemStacks.get(slot));
                }

                this.itemStacks = expandedItems;
                this.ams$sizeBeforeExpansion = currentSize;
            }

            return Math.max(original, size);
        }

        if (this.ams$sizeBeforeExpansion >= 0 && currentSize == 9 * 6) {
            for (int slot = this.ams$sizeBeforeExpansion; slot < currentSize; slot++) {
                this.itemStacks.set(slot, ItemStack.EMPTY);
            }

            if (this.openCount != 0) {
                return original;
            }

            NonNullList<@NotNull ItemStack> originalItems = NonNullList.withSize(this.ams$sizeBeforeExpansion, ItemStack.EMPTY);

            for (int slot = 0; slot < originalItems.size(); slot++) {
                originalItems.set(slot, this.itemStacks.get(slot));
            }

            this.itemStacks = originalItems;
            this.ams$sizeBeforeExpansion = -1;

            return original == currentSize ? originalItems.size() : original;
        }

        return original;
    }

    //#if MC>=11700
    @Inject(method = "tick", at = @At("HEAD"))
    private static void onTick(Level level, BlockPos pos, BlockState state, ShulkerBoxBlockEntity box, CallbackInfo ci) {
        if (!level.isClientSide()) {
            ((ShulkerBoxBlockEntityMixin) (Object) box).ams$updateRuleState();
        }
    }
    //#else
    //$$ @Inject(method = "tick", at = @At("HEAD"))
    //$$ private void updateComparatorOnRuleChange(CallbackInfo ci) {
    //$$     if (this.level != null && !this.level.isClientSide()) {
    //$$         this.ams$updateRuleState();
    //$$     }
    //$$ }
    //#endif

    @Unique
    private void ams$updateRuleState() {
        boolean enabled = CarpetAMSAdditionSettings.largeShulkerBox;
        boolean ruleChanged = this.ams$lastLargeShulkerBox != enabled;
        int previousSize = this.itemStacks.size();
        if (ruleChanged || (!enabled && this.ams$sizeBeforeExpansion >= 0)) {
            this.getContainerSize();
            this.ams$lastLargeShulkerBox = enabled;
            if (ruleChanged || previousSize != this.itemStacks.size()) {
                this.setChanged();
            }
        }
    }

    @Inject(method = "getSlotsForFace", at = @At("HEAD"), cancellable = true)
    private void getAvailableSlots(Direction side, CallbackInfoReturnable<int[]> cir) {
        if (CarpetAMSAdditionSettings.largeShulkerBox) {
            int size = this.getContainerSize();
            if (this.ams$sizeBeforeExpansion >= 0 && this.itemStacks.size() == 9 * 6) {
                cir.setReturnValue(IntStream.range(0, size).toArray());
            }
        }
    }
}
