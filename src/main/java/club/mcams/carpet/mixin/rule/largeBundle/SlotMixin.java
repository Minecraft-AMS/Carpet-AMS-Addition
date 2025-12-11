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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.Objects;

@GameVersion(version = "Minecraft >= 1.17.1")
@Mixin(Slot.class)
public abstract class SlotMixin {
    @ModifyReturnValue(method = "mayPlace", at = @At("RETURN"))
    private boolean canInsert(boolean original, ItemStack stack) {
        if (!Objects.equals(AmsServerSettings.largeBundle, "false") && ((SlotAccessor) this).getContainer() instanceof LargeBundleInventory) {
            return LargeBundleInventory.canInsert(stack);
        } else {
            return original;
        }
    }
}
