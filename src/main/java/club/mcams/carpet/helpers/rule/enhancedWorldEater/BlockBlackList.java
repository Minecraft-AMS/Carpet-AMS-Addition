/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
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

package club.mcams.carpet.helpers.rule.enhancedWorldEater;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class BlockBlackList {
    private static final List<Block> blacklist = new ArrayList<>();

    public static boolean isIn(Block block) {
        return blacklist.contains(block);
    }

    static {
        blacklist.add(Blocks.BEDROCK);
        blacklist.add(Blocks.ANVIL);
        blacklist.add(Blocks.CHIPPED_ANVIL);
        blacklist.add(Blocks.DAMAGED_ANVIL);
        blacklist.add(Blocks.END_PORTAL_FRAME);
        blacklist.add(Blocks.END_PORTAL);
        blacklist.add(Blocks.END_GATEWAY);
    }
}
