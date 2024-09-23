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

package club.mcams.carpet.api.recipe;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.api.recipe.template.ShapedRecipeTemplate;
import club.mcams.carpet.api.recipe.template.ShapelessRecipeTemplate;
import club.mcams.carpet.utils.IdentifierUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmsRecipeRegistry {
    private static final String MOD_ID = AmsServer.compactName;
    private static final AmsRecipeRegistry instance = new AmsRecipeRegistry();
    public List<ShapedRecipeTemplate> shapedRecipeList = new ArrayList<>();
    public List<ShapelessRecipeTemplate> shapelessRecipeList = new ArrayList<>();

    public static AmsRecipeRegistry getInstance() {
        return instance;
    }

    public void register() {
        // Shaped Recipe List
        if (AmsServerSettings.craftableEnchantedGoldenApples) {
            String[][] enchantedGoldenApplePattern = {
                {"#", "#", "#"},
                {"#", "A", "#"},
                {"#", "#", "#"}
            };
            Map<Character, String> enchantedGoldenAppleIngredients = new HashMap<>();
            enchantedGoldenAppleIngredients.put('#', "minecraft:gold_block");
            enchantedGoldenAppleIngredients.put('A', "minecraft:apple");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "enchanted_golden_apple"),
                enchantedGoldenApplePattern,
                enchantedGoldenAppleIngredients,
                "minecraft:enchanted_golden_apple",
                1
            ));
        }

        if (AmsServerSettings.betterCraftableBoneBlock) {
            String[][] bonePattern = {
                {"#", "#", "#"},
                {"#", "#", "#"},
                {"#", "#", "#"}
            };
            Map<Character, String> boneBlockIngredients = new HashMap<>();
            boneBlockIngredients.put('#', "minecraft:bone");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "bone_block"),
                bonePattern,
                boneBlockIngredients,
                "minecraft:bone_block",
                3
            ));
        }

        if (AmsServerSettings.betterCraftableDispenser) {
            String[][] dispenserPattern = {
                {" ", "S", "X"},
                {"S", "D", "X"},
                {" ", "S", "X"}
            };
            Map<Character, String> dispenserIngredients = new HashMap<>();
            dispenserIngredients.put('D', "minecraft:dropper");
            dispenserIngredients.put('S', "minecraft:stick");
            dispenserIngredients.put('X', "minecraft:string");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "dispenser1"),
                dispenserPattern,
                dispenserIngredients,
                "minecraft:dispenser",
                1
            ));
        }

        if (AmsServerSettings.craftableElytra) {
            String[][] elytraPattern = {
                {"P", "S", "P"},
                {"P", "*", "P"},
                {"P", "L", "P"}
            };
            Map<Character, String> elytraIngredients = new HashMap<>();
            elytraIngredients.put('P', "minecraft:phantom_membrane");
            elytraIngredients.put('S', "minecraft:stick");
            elytraIngredients.put('*', "minecraft:saddle");
            elytraIngredients.put('L', "minecraft:string");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "elytra"),
                elytraPattern,
                elytraIngredients,
                "minecraft:elytra",
                1
            ));
        }

        //#if MC<11900 && MC>=11700
        if (AmsServerSettings.craftableSculkSensor) {
            String[][] sculkSensorPattern = {
                {" ", " ", " "},
                {"R", "Q", "R"},
                {"D", "D", "D"}
            };
            Map<Character, String> sculkSensorIngredients = new HashMap<>();
            sculkSensorIngredients.put('D', "minecraft:deepslate");
            sculkSensorIngredients.put('R', "minecraft:redstone");
            sculkSensorIngredients.put('Q', "minecraft:quartz");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "sculk_sensor"),
                sculkSensorPattern,
                sculkSensorIngredients,
                "minecraft:sculk_sensor",
                1
            ));
        }
        //#endif

        // Shapeless Recipe List
        if (AmsServerSettings.betterCraftableDispenser) {
            List<String> dispenserIngredients = List.of(
                "minecraft:bow",
                "minecraft:dropper"
            );
            shapelessRecipeList.add(new ShapelessRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "dispenser2"),
                dispenserIngredients,
                "minecraft:dispenser",
                1
            ));
        }

        //#if MC>=11700 && MC<12102
        if (AmsServerSettings.craftableBundle) {
            List<String> bundleIngredients = List.of(
                "minecraft:string",
                "minecraft:leather"
            );
            shapelessRecipeList.add(new ShapelessRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "bundle"),
                bundleIngredients,
                "minecraft:bundle",
                1
            ));
        }
        //#endif

        //#if MC>=11700
        if (AmsServerSettings.betterCraftablePolishedBlackStoneButton) {
            List<String> polishedBlackstoneButtonIngredients = List.of(
                "minecraft:deepslate"
            );
            shapelessRecipeList.add(new ShapelessRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "polished_blackstone_button"),
                polishedBlackstoneButtonIngredients,
                "minecraft:polished_blackstone_button",
                1
            ));
        }
        //#endif
    }
}
