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

package club.mcams.carpet.helpers.rule.largeBundle;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.core.NonNullList;

import java.util.List;

public class LargeBundleInventory implements Container {
    private final ItemStack stack;
    private final List<ItemStack> items = NonNullList.withSize(9 * 6, ItemStack.EMPTY);

    public LargeBundleInventory(ItemStack stack) {
        this.stack = stack;
        ItemContainerContents container = stack.get(DataComponents.CONTAINER);
        if (container != null) {
            List<ItemStack> containerStacks = container.stream().toList();
            for (int i = 0; i < Math.min(containerStacks.size(), this.getContainerSize()); i++) {
                this.items.set(i, containerStacks.get(i).copy());
            }
        }
    }

    @Override
    public void setChanged() {
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    @Override
    public void setItem(int slot, ItemStack newStack) {
        this.items.set(slot, newStack.copy());
        this.setChanged();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public int getContainerSize() {
        return 9 * 6;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = ContainerHelper.takeItem(this.items, slot);
        setChanged();
        return result;
    }

    @Override
    public void clearContent() {
        this.items.clear();
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return !this.stack.isEmpty();
    }

    /**
     * 在Mixin中调用
     * @see BundleItem
     * @see club.mcams.carpet.mixin.rule.largeBundle.BundleItemMixin
     */
    public static boolean canInsert(ItemStack stack) {
        if (stack.getItem() instanceof BundleItem) {
            return false;
        }
        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock) {
            return false;
        }
        return !stack.isEmpty();
    }
}
