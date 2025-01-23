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

package club.mcams.carpet.mixin.builtin.showHoneyBlockAndSlimeBlockInCreativeInvScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.ArrayList;
import java.util.List;

// 这是一个在Minecraft 1.16.5的创造物品栏的红石选项卡下添加蜜块和黏液块的客户端功能
// TODO: 在我决定制作新的客户端模组之前，暂时（也有可能长期）内置这个功能，毕竟也只有1.16.5中存在，且代码很绿色健康
@Environment(value = EnvType.CLIENT)
@GameVersion(version = "Minecraft < 1.17")
@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin {
    @Inject(method = "setSelectedTab", at = @At("TAIL"))
    private void onSetSelectedTab(ItemGroup group, CallbackInfo ci) {
        if (group.equals(ItemGroup.REDSTONE)) {
            final ItemStack slimeBlock = new ItemStack(Items.SLIME_BLOCK);
            final ItemStack honeyBlock = new ItemStack(Items.HONEY_BLOCK);
            CreativeInventoryScreen creativeInventoryScreen = (CreativeInventoryScreen) (Object) this;
            CreativeInventoryScreen.CreativeScreenHandler creativeScreenHandler = creativeInventoryScreen.getScreenHandler();
            List<Item> list = new ArrayList<>();
            for (ItemStack itemStack : creativeScreenHandler.itemList) {
                Item item = itemStack.getItem();
                list.add(item);
            }
            int pistonIndex = list.indexOf(Items.PISTON);
            creativeScreenHandler.itemList.add(pistonIndex + 1, slimeBlock);
            creativeScreenHandler.itemList.add(pistonIndex + 2, honeyBlock);
            creativeInventoryScreen.mouseScrolled(0.01F, 0.01F, 0.01F);
        }
    }
}
