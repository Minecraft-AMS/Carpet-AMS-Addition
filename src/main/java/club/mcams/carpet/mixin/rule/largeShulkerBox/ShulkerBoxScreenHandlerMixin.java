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

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.ShulkerBoxSlot;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShulkerBoxScreenHandler.class, priority = 1024)
public abstract class ShulkerBoxScreenHandlerMixin extends ScreenHandler {
    protected ShulkerBoxScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @ModifyArg(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/screen/ScreenHandler;<init>(Lnet/minecraft/screen/ScreenHandlerType;I)V"
            ),
            index = 0
    )
    private static ScreenHandlerType<?> getScreenHandlerType(ScreenHandlerType<?> type) {
        if (!AmsServerSettings.largeShulkerBox) {
            return type;
        }
        return ScreenHandlerType.GENERIC_9X6;
    }

    @Inject(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V",
            at = @At(
                    value = "JUMP",
                    opcode = Opcodes.GOTO,
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void init(int syncId, PlayerInventory playerInventory, Inventory inventory, CallbackInfo ci) {
        if (!AmsServerSettings.largeShulkerBox || this.slots.size() != 9 * 3) {
            return;
        }
        checkSize(inventory, 9 * 6);
        for (int row = 3; row < 6; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new ShulkerBoxSlot(inventory, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }
    }
}