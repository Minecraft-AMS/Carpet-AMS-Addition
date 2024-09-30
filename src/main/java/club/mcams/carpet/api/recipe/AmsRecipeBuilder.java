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
import club.mcams.carpet.api.recipe.template.SmeltingRecipeTemplate;
import club.mcams.carpet.utils.ChainableHashMap;
import club.mcams.carpet.utils.ChainableList;
import club.mcams.carpet.utils.IdentifierUtil;

import java.util.ArrayList;
import java.util.List;

public class AmsRecipeBuilder {
    private static final String MOD_ID = AmsServer.compactName;
    private static final AmsRecipeBuilder instance = new AmsRecipeBuilder();
    public List<ShapedRecipeTemplate> shapedRecipeList = new ArrayList<>();
    public List<ShapelessRecipeTemplate> shapelessRecipeList = new ArrayList<>();
    public List<SmeltingRecipeTemplate> smeltingRecipeList = new ArrayList<>();

    public static AmsRecipeBuilder getInstance() {
        return instance;
    }

    public void build() {
        /*
         * 有序合成配方
         */
        if (AmsServerSettings.craftableEnchantedGoldenApples) {
            String[][] notchApplePattern = {
                {"#", "#", "#"},
                {"#", "A", "#"},
                {"#", "#", "#"}
            };
            ChainableHashMap<Character, String> notchAppleIngredients = new ChainableHashMap<>();
            notchAppleIngredients.cPut('#', "minecraft:gold_block").cPut('A', "minecraft:apple");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "enchanted_golden_apple"),
                notchApplePattern, notchAppleIngredients, "minecraft:enchanted_golden_apple", 1
            ));
        }

        if (AmsServerSettings.betterCraftableBoneBlock) {
            String[][] bonePattern = {
                {"#", "#", "#"},
                {"#", "#", "#"},
                {"#", "#", "#"}
            };
            ChainableHashMap<Character, String> boneBlockIngredients = new ChainableHashMap<>();
            boneBlockIngredients.cPut('#', "minecraft:bone");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "bone_block"),
                bonePattern, boneBlockIngredients, "minecraft:bone_block", 3
            ));
        }

        if (AmsServerSettings.betterCraftableDispenser) {
            String[][] dispenserPattern = {
                {" ", "S", "X"},
                {"S", "D", "X"},
                {" ", "S", "X"}
            };
            ChainableHashMap<Character, String> dispenserIngredients = new ChainableHashMap<>();
            dispenserIngredients.cPut('D', "minecraft:dropper").cPut('S', "minecraft:stick").cPut('X', "minecraft:string");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "dispenser1"),
                dispenserPattern, dispenserIngredients, "minecraft:dispenser", 1
            ));
        }

        if (AmsServerSettings.craftableElytra) {
            String[][] elytraPattern = {
                {"P", "S", "P"},
                {"P", "*", "P"},
                {"P", "L", "P"}
            };
            ChainableHashMap<Character, String> elytraIngredients = new ChainableHashMap<>();
            elytraIngredients.cPut('P', "minecraft:phantom_membrane").cPut('S', "minecraft:stick").cPut('*', "minecraft:saddle").cPut('L', "minecraft:string");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "elytra"),
                elytraPattern, elytraIngredients, "minecraft:elytra", 1
            ));
        }

        //#if MC<11900 && MC>=11700
        if (AmsServerSettings.craftableSculkSensor) {
            String[][] sculkSensorPattern = {
                {" ", " ", " "},
                {"R", "Q", "R"},
                {"D", "D", "D"}
            };
            ChainableHashMap<Character, String> sculkSensorIngredients = new ChainableHashMap<>();
            sculkSensorIngredients.cPut('D', "minecraft:deepslate").cPut('R', "minecraft:redstone").cPut('Q', "minecraft:quartz");
            shapedRecipeList.add(new ShapedRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "sculk_sensor"),
                sculkSensorPattern, sculkSensorIngredients, "minecraft:sculk_sensor", 1
            ));
        }
        //#endif

        /*
         * 无序合成配方
         */
        if (AmsServerSettings.betterCraftableDispenser) {
            ChainableList<String> dispenserIngredients = new ChainableList<>();
            dispenserIngredients.cAdd("minecraft:bow").cAdd("minecraft:dropper");
            shapelessRecipeList.add(new ShapelessRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "dispenser2"),
                dispenserIngredients, "minecraft:dispenser", 1
            ));
        }

        //#if MC>=11700 && MC<12102
        if (AmsServerSettings.craftableBundle) {
            ChainableList<String> bundleIngredients = new ChainableList<>();
            bundleIngredients.cAdd("minecraft:string").cAdd("minecraft:leather");
            shapelessRecipeList.add(new ShapelessRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "bundle"),
                bundleIngredients, "minecraft:bundle", 1
            ));
        }
        //#endif

        //#if MC>=11700
        if (AmsServerSettings.betterCraftablePolishedBlackStoneButton) {
            ChainableList<String> polishedBlackstoneButtonIngredients = new ChainableList<>();
            polishedBlackstoneButtonIngredients.cAdd("minecraft:deepslate");
            shapelessRecipeList.add(new ShapelessRecipeTemplate(
                IdentifierUtil.of(MOD_ID, "polished_blackstone_button"),
                polishedBlackstoneButtonIngredients, "minecraft:polished_blackstone_button", 1
            ));
        }
        //#endif

        /*
         * 熔炉烧炼配方
         */
        if (AmsServerSettings.rottenFleshBurnedIntoLeather) {
            smeltingRecipeList.add(
                new SmeltingRecipeTemplate(
                    IdentifierUtil.of(MOD_ID, "leather"),
                    "minecraft:rotten_flesh", "minecraft:leather",
                    0.1F, 50
                )
            );
        }
    }
}
