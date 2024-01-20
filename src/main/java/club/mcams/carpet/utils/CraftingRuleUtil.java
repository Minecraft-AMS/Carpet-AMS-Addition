/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

import carpet.CarpetServer;
//#if MC>=11900
//$$ import carpet.api.settings.CarpetRule;
//$$ import carpet.api.settings.RuleHelper;
//$$ import carpet.script.Module;
//#else
import carpet.settings.ParsedRule;
import carpet.script.bundled.BundledModule;
//#endif

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.settings.CraftingRule;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.util.WorldSavePath;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class CraftingRuleUtil {
    public static void clearAmsDataDatapacks(MinecraftServer minecraftServer) {
        File datapackPath = new File(minecraftServer.getSavePath(WorldSavePath.DATAPACKS).toString() + "/AmsData/data/");
        if (Files.isDirectory(datapackPath.toPath())) {
            try {
                FileUtils.deleteDirectory(datapackPath);
            } catch (IOException e) {
                AmsServer.LOGGER.error("Error deleting directory: " + datapackPath, e);
            }
        }
    }

    public static void loadAmsDataDatapacks(MinecraftServer minecraftServer) {
        String datapackPath = minecraftServer.getSavePath(WorldSavePath.DATAPACKS).toString();
        if (Files.isDirectory(new File(datapackPath + "/Ams_flexibleData/").toPath())) {
            try {
                FileUtils.deleteDirectory(new File(datapackPath + "/Ams_flexibleData/"));
            } catch (IOException e) {
                AmsServer.LOGGER.error("Failed to delete directory Ams_flexibleData: " + e.getMessage());
            }
        }
        datapackPath += "/AmsData/";
        boolean isFirstLoad = !Files.isDirectory(new File(datapackPath).toPath());
        try {
            Files.createDirectories(new File(datapackPath + "data/ams/recipes").toPath());
            Files.createDirectories(new File(datapackPath + "data/ams/advancements").toPath());
            Files.createDirectories(new File(datapackPath + "data/minecraft/recipes").toPath());
            copyFile("assets/carpetamsaddition/AmsRecipeTweakPack/pack.mcmeta", datapackPath + "pack.mcmeta");
        } catch (IOException e) {
            AmsServer.LOGGER.error("Failed to create directories or copy files: " + e.getMessage());
        }
        copyFile(
            "assets/carpetamsaddition/AmsRecipeTweakPack/ams/advancements/root.json",
            datapackPath + "data/ams/advancements/root.json"
        );
        for (Field f : AmsServerSettings.class.getDeclaredFields()) {
            CraftingRule craftingRule = f.getAnnotation(CraftingRule.class);
            if (craftingRule == null) continue;
            registerCraftingRule(
                craftingRule.name().isEmpty() ? f.getName() : craftingRule.name(),
                craftingRule.recipes(),
                craftingRule.recipeNamespace(),
                datapackPath + "data/"
            );
        }
        reload();
        if (isFirstLoad) {
            minecraftServer.getCommandManager().execute(minecraftServer.getCommandSource(), "/datapack enable \"file/AmsData\"");
        }
    }

    private static void registerCraftingRule(String ruleName, String[] recipes, String recipeNamespace, String dataPath) {
        updateCraftingRule(CarpetServer.settingsManager.getRule(ruleName), recipes, recipeNamespace, dataPath, ruleName);
        CarpetServer.settingsManager.addRuleObserver(
            (source, rule, s) -> {
                //#if MC>=11900
                //$$ if (rule.name().equals(ruleName)) {
                //#else
                if (rule.name.equals(ruleName)) {
                //#endif
                    updateCraftingRule(rule, recipes, recipeNamespace, dataPath, ruleName);
                    reload();
                }
            }
        );
    }

    private static void updateCraftingRule(ParsedRule<?> rule, String[] recipes, String recipeNamespace, String datapackPath, String ruleName) {
        ruleName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, ruleName);
        //#if MC>=11900
        //$$ if (rule.type() == String.class) {
        //$$ String value = RuleHelper.toRuleString(rule.value());
        //#else
        if (rule.type == String.class) {
            String value = rule.getAsString();
        //#endif
            List<String> installedRecipes = Lists.newArrayList();
            try {
                Stream<Path> fileStream = Files.list(new File(datapackPath + recipeNamespace, "recipes").toPath());
                fileStream.forEach(( path -> {
                    for (String recipeName : recipes) {
                        String fileName = path.getFileName().toString();
                        if (fileName.startsWith(recipeName)) {
                            installedRecipes.add(fileName);
                        }
                    }
                } ));
                fileStream.close();
            } catch (IOException e) {
                AmsServer.LOGGER.error("Failed to list recipes in directory: " + e.getMessage());
            }
            deleteRecipes(installedRecipes.toArray(new String[0]), recipeNamespace, datapackPath, ruleName, false);
            if (recipeNamespace.equals("ams")) {
                List<String> installedAdvancements = Lists.newArrayList();
                try {
                    Stream<Path> fileStream = Files.list(new File(datapackPath, "ams/advancements").toPath());
                    String finalRuleName = ruleName;
                    fileStream.forEach(( path -> {
                        String fileName = path.getFileName().toString().replace(".json", "");
                        if (fileName.startsWith(finalRuleName)) {
                            installedAdvancements.add(fileName);
                        }
                    } ));
                    fileStream.close();
                } catch (IOException e) {
                    AmsServer.LOGGER.error("Failed to list advancements in directory: " + e.getMessage());
                }
                for (String advancement : installedAdvancements.toArray(new String[0])) {
                    removeAdvancement(datapackPath, advancement);
                }
            }
            if (!value.equals("off")) {
                List<String> tempRecipes = Lists.newArrayList();
                for (String recipeName : recipes) {
                    tempRecipes.add(recipeName + "_" + value + ".json");
                }
                copyRecipes(tempRecipes.toArray(new String[0]), recipeNamespace, datapackPath, ruleName + "_" + value);
            }
        }
        //#if MC>=11900
        //$$ if (rule.type() == Integer.class && (Integer) rule.value() > 0) {
        //#else
        if (rule.type == int.class && (Integer) rule.get() > 0) {
        //#endif
            copyRecipes(recipes, recipeNamespace, datapackPath, ruleName);
            int value = (Integer) rule.get();
            for (String recipeName : recipes) {
                String filePath = datapackPath + recipeNamespace + "/recipes/" + recipeName;
                JsonObject jsonObject = readJson(filePath);
                assert jsonObject != null;
                jsonObject.getAsJsonObject("result").addProperty("count", value);
                writeJson(jsonObject, filePath);
            }
        //#if MC>=11900
        //$$ } else if (rule.type() == Boolean.class && RuleHelper.getBooleanValue(rule)) {
        //#else
        } else if (rule.type == boolean.class && rule.getBoolValue()) {
        //#endif
            copyRecipes(recipes, recipeNamespace, datapackPath, ruleName);
        } else {
            deleteRecipes(recipes, recipeNamespace, datapackPath, ruleName, true);
        }
    }

    private static void writeAdvancement(String datapackPath, String ruleName, String[] recipes) {
        copyFile(
                "assets/carpetamsaddition/AmsRecipeTweakPack/ams/advancements/recipe_rule.json",
                datapackPath + "ams/advancements/" + ruleName + ".json"
        );
        JsonObject advancementJson = readJson(datapackPath + "ams/advancements/" + ruleName + ".json");
        if (advancementJson != null) {
            JsonArray recipeRewards = advancementJson.getAsJsonObject("rewards").getAsJsonArray("recipes");
            for (String recipeName : recipes) {
                recipeRewards.add("ams:" + recipeName.replace(".json", ""));
            }
            writeJson(advancementJson, datapackPath + "ams/advancements/" + ruleName + ".json");
        } else {
            JsonObject defaultJson = new JsonObject();
            writeJson(defaultJson, datapackPath + "ams/advancements/" + ruleName + ".json");
        }
    }

    private static void removeAdvancement(String datapackPath, String ruleName) {
        try {
            Files.deleteIfExists(new File(datapackPath + "ams/advancements/" + ruleName + ".json").toPath());
        } catch (IOException e) {
            AmsServer.LOGGER.error("Failed to delete advancement file: " + ruleName + ".json: " + e.getMessage());
        }
    }

    private static void reload() {
        ResourcePackManager resourcePackManager = AmsServer.minecraftServer.getDataPackManager();
        resourcePackManager.scanPacks();
        Collection<String> collection = Lists.newArrayList(resourcePackManager.getEnabledNames());
        collection.add("AmsData");
        ReloadCommand.tryReloadDataPacks(collection, AmsServer.minecraftServer.getCommandSource());
    }

    private static void copyFile(String resourcePath, String targetPath) {
        InputStream source = BundledModule.class.getClassLoader().getResourceAsStream(resourcePath);
        Path target = new File(targetPath).toPath();
        try {
            assert source != null;
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            AmsServer.LOGGER.error("Resource '" + resourcePath + "' not found.");
        } catch (NullPointerException e) {
            AmsServer.LOGGER.error("Resource '" + resourcePath + "' is null.");
        }
    }

    private static void deleteRecipes(String[] recipes, String recipeNamespace, String datapackPath, String ruleName, boolean removeAdvancement) {
        for (String recipeName : recipes) {
            try {
                Files.deleteIfExists(new File(datapackPath + recipeNamespace + "/recipes", recipeName).toPath());
            } catch (IOException e) {
                AmsServer.LOGGER.error("Failed to delete recipe file " + recipeName + ": " + e.getMessage());
            }
        }
        if (removeAdvancement && recipeNamespace.equals("ams")) {
            removeAdvancement(datapackPath, ruleName);
        }
    }

    private static void copyRecipes(String[] recipes, String recipeNamespace, String datapackPath, String ruleName) {
        for (String recipeName : recipes) {
            copyFile(
                    "assets/carpetamsaddition/AmsRecipeTweakPack/" + recipeNamespace + "/recipes/" + recipeName,
                    datapackPath + recipeNamespace + "/recipes/" + recipeName
            );
        }
        if (recipeNamespace.equals("ams")) {
            writeAdvancement(datapackPath, ruleName, recipes);
        }
    }

    private static JsonObject readJson(String filePath) {
        //#if MC<11800
        //$$ JsonParser jsonParser = new JsonParser();
        //#endif
        try {
            FileReader reader = new FileReader(filePath);
            //#if MC<11800
            //$$ return jsonParser.parse(reader).getAsJsonObject();
            //#else
            return JsonParser.parseReader(reader).getAsJsonObject();
            //#endif
        } catch (FileNotFoundException e) {
            AmsServer.LOGGER.error("File not found: {}", filePath, e);
        }
        return null;
    }

    private static void writeJson(JsonObject jsonObject, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
            writer.close();
        } catch (IOException e) {
            AmsServer.LOGGER.error("Failed to write JSON to file '" + filePath + "': " + e.getMessage());
        }
    }
}
