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

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.ShulkerBoxSlot;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShulkerBoxMenu.class, priority = 1024)
public abstract class ShulkerBoxMenuMixin extends AbstractContainerMenu {
    protected ShulkerBoxMenuMixin(@Nullable MenuType<?> type, int syncId) {
        super(type, syncId);
    }

    @Unique
    private static final boolean ENABLE_FLAG$AMS;

    static {
        ENABLE_FLAG$AMS = CarpetAMSAdditionSettings.largeShulkerBox;
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;<init>(Lnet/minecraft/world/inventory/MenuType;I)V"
        ),
        index = 0
    )
    private static MenuType<?> getScreenHandlerType(MenuType<?> type) {
        if (!ENABLE_FLAG$AMS) {
            return type;
        }
        return MenuType.GENERIC_9x6;
    }

    @ModifyArg(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ShulkerBoxMenu;checkContainerSize(Lnet/minecraft/world/Container;I)V"
        ),
        index = 1
    )
    private int checkLargerSize(int size) {
        if (ENABLE_FLAG$AMS) {
            return 9 * 6;
        } else {
            return size;
        }
    }

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/Container;startOpen(Lnet/minecraft/world/entity/ContainerUser;)V",
            shift = At.Shift.AFTER
        )
    )
    protected void addingExtraSlots(int syncId, Inventory playerInventory, Container inventory, CallbackInfo ci) {
        if (ENABLE_FLAG$AMS && this.slots.isEmpty()) {
            for (int row = 3; row < 6; ++row) {
                for (int column = 0; column < 9; ++column) {
                    this.addSlot(new ShulkerBoxSlot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
                }
            }
        }
    }
}
