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

package carpetamsaddition.api.recipe;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.api.recipe.template.ShapedRecipeTemplate;
import carpetamsaddition.api.recipe.template.ShapelessRecipeTemplate;
import carpetamsaddition.api.recipe.template.SmeltingRecipeTemplate;
import carpetamsaddition.utils.ChainableHashMap;
import carpetamsaddition.utils.ChainableList;
import carpetamsaddition.utils.IdentifierUtil;

import java.util.ArrayList;
import java.util.List;

public class AmsRecipeBuilder {
    private static final String MOD_ID = CarpetAMSAdditionServer.compactName;
    private static final AmsRecipeBuilder INSTANCE = new AmsRecipeBuilder();
    private static final List<ShapedRecipeTemplate> shapedRecipeList = new ArrayList<>();
    private static final List<ShapelessRecipeTemplate> shapelessRecipeList = new ArrayList<>();
    private static final List<SmeltingRecipeTemplate> smeltingRecipeList = new ArrayList<>();

    private AmsRecipeBuilder() {}

    public static AmsRecipeBuilder getInstance() {
        return INSTANCE;
    }

    public List<ShapedRecipeTemplate> getShapedRecipeList() {
        return shapedRecipeList;
    }

    public List<ShapelessRecipeTemplate> getShapelessRecipeList() {
        return shapelessRecipeList;
    }

    public List<SmeltingRecipeTemplate> getSmeltingRecipeList() {
        return smeltingRecipeList;
    }

    public void addShapedRecipe(String id, String[][] pattern, ChainableHashMap<Character, String> ingredients, String result, int count) {
        shapedRecipeList.add(new ShapedRecipeTemplate(IdentifierUtil.of(MOD_ID, id), pattern, ingredients, result, count));
    }

    public void addShapelessRecipe(String id, ChainableList<String> ingredients, String result, int count) {
        shapelessRecipeList.add(new ShapelessRecipeTemplate(IdentifierUtil.of(MOD_ID, id), ingredients, result, count));
    }

    public void addSmeltingRecipe(String id, String input, String output, float experience, int cookingTime) {
        smeltingRecipeList.add(new SmeltingRecipeTemplate(IdentifierUtil.of(MOD_ID, id), input, output, experience, cookingTime));
    }
}
