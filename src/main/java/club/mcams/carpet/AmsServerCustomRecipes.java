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

package club.mcams.carpet;

import club.mcams.carpet.api.recipe.AmsRecipeBuilder;
import club.mcams.carpet.utils.ChainableHashMap;
import club.mcams.carpet.utils.ChainableList;

import net.minecraft.item.Item;

import static net.minecraft.item.Items.*;

public class AmsServerCustomRecipes {
    private static final AmsServerCustomRecipes INSTANCE = new AmsServerCustomRecipes();
    private static final AmsRecipeBuilder builder = AmsRecipeBuilder.getInstance();

    private AmsServerCustomRecipes() {}

    public static AmsServerCustomRecipes getInstance() {
        return INSTANCE;
    }

    public void buildRecipes() {
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
            notchAppleIngredients.cPut('#', item(GOLD_BLOCK)).cPut('A', item(APPLE));
            builder.addShapedRecipe("enchanted_golden_apple", notchApplePattern, notchAppleIngredients, item(ENCHANTED_GOLDEN_APPLE), 1);
        }

        if (AmsServerSettings.betterCraftableBoneBlock) {
            String[][] boneBlockPattern = {
                {"#", "#", "#"},
                {"#", "#", "#"},
                {"#", "#", "#"}
            };
            ChainableHashMap<Character, String> boneBlockIngredients = new ChainableHashMap<>();
            boneBlockIngredients.cPut('#', item(BONE));
            builder.addShapedRecipe("bone_block", boneBlockPattern, boneBlockIngredients, item(BONE_BLOCK), 3);
        }

        if (AmsServerSettings.betterCraftableDispenser) {
            String[][] dispenserPattern = {
                {" ", "S", "X"},
                {"S", "D", "X"},
                {" ", "S", "X"}
            };
            ChainableHashMap<Character, String> dispenserIngredients = new ChainableHashMap<>();
            dispenserIngredients.cPut('D', item(DROPPER)).cPut('S', item(STICK)).cPut('X', item(STRING));
            builder.addShapedRecipe("dispenser1", dispenserPattern, dispenserIngredients, item(DISPENSER), 1);
        }

        if (AmsServerSettings.craftableElytra) {
            String[][] elytraPattern = {
                {"P", "S", "P"},
                {"P", "*", "P"},
                {"P", "L", "P"}
            };
            ChainableHashMap<Character, String> elytraIngredients = new ChainableHashMap<>();
            elytraIngredients.cPut('P', item(PHANTOM_MEMBRANE)).cPut('S', item(STICK)).cPut('*', item(SADDLE)).cPut('L', item(STRING));
            builder.addShapedRecipe("elytra", elytraPattern, elytraIngredients, item(ELYTRA), 1);
        }

        //#if MC<11900 && MC>=11700
        if (AmsServerSettings.craftableSculkSensor) {
            String[][] sculkSensorPattern = {
                {" ", " ", " "},
                {"R", "Q", "R"},
                {"D", "D", "D"}
            };
            ChainableHashMap<Character, String> sculkSensorIngredients = new ChainableHashMap<>();
            sculkSensorIngredients.cPut('D', item(DEEPSLATE)).cPut('R', item(REDSTONE)).cPut('Q', item(QUARTZ));
            builder.addShapedRecipe("sculk_sensor", sculkSensorPattern, sculkSensorIngredients, item(SCULK_SENSOR), 1);
        }
        //#endif

        /*
         * 无序合成配方
         */
        if (AmsServerSettings.betterCraftableDispenser) {
            ChainableList<String> dispenserIngredients = new ChainableList<>();
            dispenserIngredients.cAdd(item(BOW)).cAdd(item(DROPPER));
            builder.addShapelessRecipe("dispenser2", dispenserIngredients, item(DISPENSER), 1);
        }

        //#if MC>=11700 && MC<12102
        if (AmsServerSettings.craftableBundle) {
            ChainableList<String> bundleIngredients = new ChainableList<>();
            bundleIngredients.cAdd(item(STRING)).cAdd(item(LEATHER));
            builder.addShapelessRecipe("bundle", bundleIngredients, item(BUNDLE), 1);
        }
        //#endif

        //#if MC>=11700
        if (AmsServerSettings.betterCraftablePolishedBlackStoneButton) {
            ChainableList<String> polishedBlackstoneButtonIngredients = new ChainableList<>();
            polishedBlackstoneButtonIngredients.cAdd(item(DEEPSLATE));
            builder.addShapelessRecipe("polished_blackstone_button", polishedBlackstoneButtonIngredients, item(POLISHED_BLACKSTONE_BUTTON), 1);
        }
        //#endif

        /*
         * 熔炉烧炼配方
         */
        if (AmsServerSettings.rottenFleshBurnedIntoLeather) {
            builder.addSmeltingRecipe("leather", item(ROTTEN_FLESH), item(LEATHER), 0.1F, 50);
        }
    }

    private static String item(Item item) {
        return item.toString();
    }
}
