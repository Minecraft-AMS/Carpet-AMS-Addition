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

package carpetamsaddition.helpers.rule.largeEnderChest;

import carpetamsaddition.mixin.rule.largeEnderChest.SimpleContainerAccessor;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public final class LargeEnderChestInventory {
    private static final int LARGE_SIZE = 9 * 6;

    private LargeEnderChestInventory() {}

    public static void ensureCapacity(PlayerEnderChestContainer inventory) {
        int size = inventory.getContainerSize();
        if (size >= LARGE_SIZE) {
            return;
        }

        NonNullList<@NotNull ItemStack> items = NonNullList.withSize(LARGE_SIZE, ItemStack.EMPTY);
        for (int slot = 0; slot < size; slot++) {
            items.set(slot, inventory.getItem(slot));
        }

        SimpleContainerAccessor accessor = (SimpleContainerAccessor) inventory;
        accessor.setStacks(items);
        accessor.setSize(LARGE_SIZE);
    }
}
