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

package carpetamsaddition.mixin.rule.largeEnderChest;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.helpers.rule.largeEnderChest.LargeEnderChestInventory;

import net.minecraft.world.inventory.PlayerEnderChestContainer;
//#if MC>=12108
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.level.storage.ValueInput;
//#else
//$$ import net.minecraft.nbt.ListTag;
//#endif

//#if MC>=12006
//$$ import net.minecraft.core.HolderLookup;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEnderChestContainer.class)
public abstract class PlayerEnderChestContainerMixin {
    //#if MC>=12108
    @Inject(method = "fromSlots", at = @At("HEAD"))
    private void expandWhenEnabled(ValueInput.TypedInputList<ItemStackWithSlot> storedItems, CallbackInfo ci) {
        if (CarpetAMSAdditionSettings.largeEnderChest) {
            LargeEnderChestInventory.ensureCapacity((PlayerEnderChestContainer) (Object) this);
        }
    }
    //#else
    //$$ @Inject(method = "fromTag", at = @At("HEAD"))
    //$$ private void expandWhenEnabled(
    //$$     ListTag storedItems,
    //#if MC>=12006
    //$$     HolderLookup.Provider registries,
    //#endif
    //$$     CallbackInfo ci
    //$$ ) {
    //$$     if (CarpetAMSAdditionSettings.largeEnderChest) {
    //$$         LargeEnderChestInventory.ensureCapacity((PlayerEnderChestContainer) (Object) this);
    //$$     }
    //$$ }
    //#endif
}
