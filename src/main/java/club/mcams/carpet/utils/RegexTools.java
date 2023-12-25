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

package club.mcams.carpet.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTools {
    public static String getBlockRegisterName(String sourceName) {
        String regex = "\\{(.*?)}";     //Block{minecraft:bedrock} -> minecraft:bedrock
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sourceName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return sourceName;
    }

    public static Map<String, Float> parseFloatValues(String customBlock) {
        Map<String, Float> moreCustomBlock = new HashMap<>();
        for (String s : customBlock.split(",")) {
            String[] parts = s.split("/");      //minecraft:bedrock/2.0 -> 获取 2.0
            if (parts.length == 2) {
                moreCustomBlock.put(parts[0], Float.parseFloat(parts[1]));
            }
        }
        return moreCustomBlock;
    }
}
