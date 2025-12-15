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

package carpetamsaddition;

import carpetamsaddition.api.recipe.builder.ShapedRecipeBuilder;
import carpetamsaddition.api.recipe.builder.ShapelessRecipeBuilder;
import carpetamsaddition.api.recipe.builder.SmeltingRecipeBuilder;

import static net.minecraft.world.item.Items.*;

public class CarpetAMSAdditionCustomRecipes {
    private static final CarpetAMSAdditionCustomRecipes INSTANCE = new CarpetAMSAdditionCustomRecipes();

    private CarpetAMSAdditionCustomRecipes() {}

    public static CarpetAMSAdditionCustomRecipes getInstance() {
        return INSTANCE;
    }

    public void buildRecipes() {
        ShapedRecipeBuilder.create(CarpetAMSAdditionSettings.craftableEnchantedGoldenApples, "enchanted_golden_apple")
        .pattern("###")
        .pattern("#A#")
        .pattern("###")
        .define('#', GOLD_BLOCK).define('A', APPLE)
        .output(ENCHANTED_GOLDEN_APPLE, 1).build();

        ShapedRecipeBuilder.create(CarpetAMSAdditionSettings.betterCraftableBoneBlock, "bone_block")
        .pattern("###")
        .pattern("###")
        .pattern("###")
        .define('#', BONE)
        .output(BONE_BLOCK, 3).build();

        ShapedRecipeBuilder.create(CarpetAMSAdditionSettings.betterCraftableDispenser, "dispenser1")
        .pattern(" SX")
        .pattern("SDX")
        .pattern(" SX")
        .define('D', DROPPER).define('S', STICK).define('X', STRING)
        .output(DISPENSER, 1).build();

        ShapedRecipeBuilder.create(CarpetAMSAdditionSettings.craftableElytra, "elytra")
        .pattern("PSP")
        .pattern("P*P")
        .pattern("PLP")
        .define('P', PHANTOM_MEMBRANE).define('S', STICK).define('*', SADDLE).define('L', STRING)
        .output(ELYTRA, 1).build();

        /*
         * 无序合成配方
         */
        ShapelessRecipeBuilder.create(CarpetAMSAdditionSettings.betterCraftableDispenser, "dispenser2")
        .addIngredient(BOW).addIngredient(DROPPER).output(DISPENSER, 1).build();

        ShapelessRecipeBuilder.create(CarpetAMSAdditionSettings.betterCraftablePolishedBlackStoneButton, "polished_blackstone_button")
        .addIngredient(DEEPSLATE).output(POLISHED_BLACKSTONE_BUTTON, 1).build();

        // Mixin实现
        ShapelessRecipeBuilder.create(CarpetAMSAdditionSettings.craftableCarvedPumpkin, "carved_pumpkin")
        .addIngredient(SHEARS).addIngredient(PUMPKIN).output(CARVED_PUMPKIN, 1).build();

        /*
         * 熔炉烧炼配方
         */
        SmeltingRecipeBuilder.create(CarpetAMSAdditionSettings.rottenFleshBurnedIntoLeather, "leather")
        .material(ROTTEN_FLESH).experience(0.1F).cookTime(50).output(LEATHER, 1).build();
    }
}
