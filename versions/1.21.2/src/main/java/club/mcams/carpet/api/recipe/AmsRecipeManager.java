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
import club.mcams.carpet.api.recipe.template.ShapedRecipeTemplate;
import club.mcams.carpet.api.recipe.template.ShapelessRecipeTemplate;
import club.mcams.carpet.api.recipe.template.SmeltingRecipeTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GameVersion(version = "Minecraft >= 1.21.2")
public class AmsRecipeManager {
    private final List<ShapelessRecipeTemplate> shapelessRecipes;
    private final List<ShapedRecipeTemplate> shapedRecipes;
    private final List<SmeltingRecipeTemplate> smeltingRecipes;

    public AmsRecipeManager(AmsRecipeBuilder builder) {
        this.shapelessRecipes = builder.getShapelessRecipeList();
        this.shapedRecipes = builder.getShapedRecipeList();
        this.smeltingRecipes = builder.getSmeltingRecipeList();
    }

    public void registerRecipes(Map<Identifier, Recipe<?>> map, RegistryWrapper.WrapperLookup wrapperLookup) {
        Map<Identifier, JsonElement> recipeMap = new HashMap<>();
        registerAllRecipes(recipeMap);
        recipeMap.forEach((id, json) -> deserializeRecipe(map, wrapperLookup, id, json));
    }

    @SuppressWarnings("DuplicatedCode")
    private void deserializeRecipe(Map<Identifier, Recipe<?>> map, RegistryWrapper.WrapperLookup wrapperLookup, Identifier id, JsonElement json) {
        try {
            RecipeEntry<?> recipeEntry = ServerRecipeManager.deserialize(RegistryKey.of(RegistryKeys.RECIPE, id), json.getAsJsonObject(), wrapperLookup);
            map.put(id, recipeEntry.value());
        } catch (JsonParseException e) {
            AmsServer.LOGGER.warn("Failed to parse recipe: {}, error: {}", id, e.getMessage());
        } catch (Exception e) {
            AmsServer.LOGGER.error("Unexpected error during recipe deserialization: {}, error: {}", id, e);
        }
    }

    private void registerAllRecipes(Map<Identifier, JsonElement> recipeMap) {
        shapelessRecipes.forEach(recipe -> recipe.addToRecipeMap(recipeMap));
        shapedRecipes.forEach(recipe -> recipe.addToRecipeMap(recipeMap));
        smeltingRecipes.forEach(recipe -> recipe.addToRecipeMap(recipeMap));
    }

    public static void clearRecipeListMemory(AmsRecipeBuilder amsRecipeBuilder) {
        amsRecipeBuilder.getShapedRecipeList().clear();
        amsRecipeBuilder.getShapelessRecipeList().clear();
        amsRecipeBuilder.getSmeltingRecipeList().clear();
    }
}
