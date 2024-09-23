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

import club.mcams.carpet.api.recipe.template.ShapedRecipeTemplate;
import club.mcams.carpet.api.recipe.template.ShapelessRecipeTemplate;

import com.google.gson.JsonElement;
//#if MC>=12102
//$$ import club.mcams.carpet.AmsServer;
//$$ import com.google.gson.JsonParseException;
//$$ import net.minecraft.recipe.Recipe;
//$$ import net.minecraft.recipe.RecipeEntry;
//$$ import net.minecraft.recipe.RecipeManager;
//$$ import net.minecraft.registry.RegistryWrapper;
//$$ import java.util.HashMap;
//#endif
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class AmsRecipeManager {
    private final List<ShapelessRecipeTemplate> shapelessRecipes;
    private final List<ShapedRecipeTemplate> shapedRecipes;

    public AmsRecipeManager(List<ShapelessRecipeTemplate> shapelessRecipes, List<ShapedRecipeTemplate> shapedRecipes) {
        this.shapelessRecipes = shapelessRecipes;
        this.shapedRecipes = shapedRecipes;
    }

    public void registerRecipes(
        //#if MC>=12102
        //$$ Map<Identifier, Recipe<?>> map, RegistryWrapper.WrapperLookup wrapperLookup
        //#else
        Map<Identifier, JsonElement> recipeMap
        //#endif
    ) {
        //#if MC>=12102
        //$$ Map<Identifier, JsonElement> recipeMap = new HashMap<>();
        //#endif
        for (ShapelessRecipeTemplate recipe : shapelessRecipes) {
            recipe.addToRecipeMap(recipeMap);
        }
        for (ShapedRecipeTemplate recipe : shapedRecipes) {
            recipe.addToRecipeMap(recipeMap);
        }
        //#if MC>=12102
        //$$ for (Map.Entry<Identifier, JsonElement> entry : recipeMap.entrySet()) {
        //$$     Identifier id = entry.getKey();
        //$$     JsonElement json = entry.getValue();
        //$$     try {
        //$$         RecipeEntry<?> recipeEntry = RecipeManager.deserialize(id, json.getAsJsonObject(), wrapperLookup);
        //$$         map.put(id, recipeEntry.value());
        //$$     } catch (JsonParseException e) {
        //$$         AmsServer.LOGGER.warn("Failed to parse recipe: {}", id);
        //$$     }
        //$$ }
        //#endif
    }

    public static void clearRecipeListMemory(AmsRecipeRegistry amsRecipeRegistry) {
        amsRecipeRegistry.shapedRecipeList.clear();
        amsRecipeRegistry.shapelessRecipeList.clear();
    }
}
