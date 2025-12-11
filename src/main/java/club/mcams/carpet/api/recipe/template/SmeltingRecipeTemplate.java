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

package club.mcams.carpet.api.recipe.template;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.Identifier;

import java.util.Map;

public class SmeltingRecipeTemplate implements RecipeTemplateInterface {
    private final Identifier recipeId;
    private final String ingredient;
    private final String resultItem;
    private final float experience;
    private final int cookingTime;

    public SmeltingRecipeTemplate(Identifier recipeId, String ingredient, String resultItem, float experience, int cookingTime) {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.resultItem = resultItem;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public JsonObject toJson() {
        JsonObject recipeJson = new JsonObject();
        recipeJson.addProperty("type", "minecraft:smelting");

        recipeJson.addProperty("ingredient", ingredient);

        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("id", resultItem);
        recipeJson.add("result", resultJson);

        recipeJson.addProperty("experience", experience);
        recipeJson.addProperty("cookingtime", cookingTime);

        return recipeJson;
    }

    @Override
    public void addToRecipeMap(Map<Identifier, JsonElement> recipeMap) {
        recipeMap.put(recipeId, toJson());
    }
}
