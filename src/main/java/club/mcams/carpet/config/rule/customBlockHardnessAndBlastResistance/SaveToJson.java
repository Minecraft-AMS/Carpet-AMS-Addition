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

package club.mcams.carpet.config.rule.customBlockHardnessAndBlastResistance;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.utils.RegexTools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.BlockState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SaveToJson {
    @SuppressWarnings("ReadWriteStringCanBeUsed")
    public static void save(Map<BlockState, Float> customBlockMap, String configFilePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Float> simplifiedMap = new HashMap<>();
        for (Map.Entry<BlockState, Float> entry : customBlockMap.entrySet()) {
            BlockState state = entry.getKey().getBlock().getDefaultState();
            String blockName = RegexTools.getBlockRegisterName(state.getBlock().toString());
            simplifiedMap.put(blockName, entry.getValue());
        }
        String json = gson.toJson(simplifiedMap);
        try {
            Path path = Paths.get(configFilePath);
            Files.createDirectories(path.getParent());
            Files.write(path, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            AmsServer.LOGGER.warn("Failed to save config", e);
        }
    }
}
