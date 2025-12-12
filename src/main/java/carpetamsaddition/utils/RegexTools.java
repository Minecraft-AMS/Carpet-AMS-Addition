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

package carpetamsaddition.utils;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;

public class RegexTools {
    //state.getBlock.toString(); | Block{minecraft:bedrock} -> minecraft:bedrock
    public static String getBlockRegisterName(String sourceName) {
        return sourceName.replaceAll(".*\\{(.*?)}.*", "$1");
    }

    public static String getBlockRegisterName(BlockState blockState) {
        return blockState.getBlock().toString().replaceAll(".*\\{(.*?)}.*", "$1");
    }

    // itemStack.getItem.toString() | 5 minecraft:bedrock -> minecraft:bedrock
    public static String getItemRegisterName(String sourceName) {
        return sourceName.replaceAll(".*?(minecraft:[a-z_]+).*", "$1");
    }

    public static String getItemRegisterName(ItemStack itemStack) {
        return BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString().replaceAll(".*?(minecraft:[a-z_]+).*", "$1");
    }
}
