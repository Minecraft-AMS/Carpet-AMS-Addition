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

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
//#if MC>=12005
//$$ import net.minecraft.component.DataComponentTypes;
//$$ import net.minecraft.component.type.ContainerComponent;
//$$ import java.util.List;
//#else
import net.minecraft.nbt.NbtCompound;
//#endif
import net.minecraft.util.collection.DefaultedList;

public class LargeBundleInventory implements Inventory {
    private final ItemStack stack;
    //#if MC>=12005
    //$$ private final List<ItemStack> items = DefaultedList.ofSize(9 * 6, ItemStack.EMPTY);
    //#else
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9 * 6, ItemStack.EMPTY);
    //#endif

    public LargeBundleInventory(ItemStack stack) {
        this.stack = stack;
        //#if MC>=12005
        //$$ ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        //$$ if (container != null) {
        //$$     List<ItemStack> containerStacks = container.stream().toList();
        //$$     for (int i = 0; i < Math.min(containerStacks.size(), this.size()); i++) {
        //$$        if (this.items != null) {
        //$$            this.items.set(i, containerStacks.get(i).copy());
        //$$        }
        //$$     }
        //$$ }
        //#else
        NbtCompound tag = stack.getOrCreateNbt();
        if (tag.contains("Items")) {
            Inventories.readNbt(tag, this.items);
        }
        //#endif
    }

    @Override
    public void markDirty() {
        //#if MC>=12005
        //$$ stack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(items));
        //#else
        NbtCompound tag = stack.getOrCreateNbt();
        Inventories.writeNbt(tag, this.items, true);
        stack.setNbt(tag);
        //#endif
    }

    @Override
    public void setStack(int slot, ItemStack newStack) {
        this.items.set(slot, newStack.copy());
        this.markDirty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.items.get(slot);
    }

    @Override
    public int size() {
        return 9 * 6;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(this.items, slot, amount);
        markDirty();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(this.items, slot);
        markDirty();
        return result;
    }

    @Override
    public void clear() {
        this.items.clear();
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return !this.stack.isEmpty();
    }

    /**
     * 在Mixin中调用
     * @see net.minecraft.item.BundleItem
     * @see club.mcams.carpet.mixin.rule.largeBundle.BundleItemMixin
     */
    public static boolean canInsert(ItemStack stack) {
        if (stack.getItem() instanceof BundleItem) {
            return false;
        }
        if (Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock) {
            return false;
        }
        return !stack.isEmpty();
    }
}
