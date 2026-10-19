/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.helpers.rule.commandLocationTag;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
//#if MC<12000
//$$ import carpetamsaddition.mixin.rule.commandLocationTag.SignBlockEntityAccessor;
//#endif
//#if MC>=260300
//$$ import net.minecraft.world.level.block.entity.SignTextSlot;
//#endif

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LocationTagSignCompat {
    private LocationTagSignCompat() {}

    public static String line(SignBlockEntity sign, int line) {
        //#if MC>=260300
        //$$ return sign.getText(SignTextSlot.FRONT).getMessages(false).get(line).getString();
        //#elseif MC>=12000
        return sign.getText(true).getMessage(line, false).getString();
        //#else
        //$$ return ((SignBlockEntityAccessor) sign).ams$getLocationTagMessages()[line].getString();
        //#endif
    }

    public static String type(Block block) {
        String id = BuiltInRegistries.BLOCK.getKey(block).toString();
        return id.replace("_wall_hanging_sign", "_hanging_sign").replace("_wall_sign", "_sign");
    }

    public static List<String> types() {
        List<String> types = new ArrayList<>();

        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof SignBlock) {
                String type = type(block);
                if (!types.contains(type)) {
                    types.add(type);
                }
            }
        }

        Collections.sort(types);

        return types;
    }
}
