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

package carpetamsaddition.api.recipe.template;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

/**
 * 无序合成配方模板。
 * 保存原料物品注册名列表与产物信息，
 * 可将其序列化为 minecraft:crafting_shapeless 类型的配方 JSON（原料顺序无关）。
 */
public class ShapelessRecipeTemplate implements RecipeTemplateInterface {
    /** 配方唯一标识 */
    private final Identifier recipeId;
    /** 原料物品注册名列表 */
    private final List<String> ingredients;
    /** 产物物品注册名 */
    private final String resultItem;
    /** 产物数量 */
    private final int resultCount;

    /**
     * @param recipeId    配方唯一标识
     * @param ingredients 原料物品注册名列表
     * @param resultItem  产物物品注册名
     * @param resultCount 产物数量
     */
    public ShapelessRecipeTemplate(Identifier recipeId, List<String> ingredients, String resultItem, int resultCount) {
        this.recipeId = recipeId;
        this.ingredients = ingredients;
        this.resultItem = resultItem;
        this.resultCount = resultCount;
    }

    /** 序列化为无序合成配方 JSON（ingredients / result 结构）。 */
    @Override
    public JsonObject toJson() {
        JsonObject recipeJson = new JsonObject();
        recipeJson.addProperty("type", "minecraft:crafting_shapeless");

        JsonArray ingredientsJson = new JsonArray();

        for (String ingredient : ingredients) {
            //#if MC>=12102
            ingredientsJson.add(ingredient);
            //#else
            //$$ JsonObject ingredientJson = new JsonObject();
            //$$ ingredientJson.addProperty("item", ingredient);
            //$$ ingredientsJson.add(ingredientJson);
            //#endif
        }

        recipeJson.add("ingredients", ingredientsJson);

        JsonObject resultJson = new JsonObject();
        resultJson.addProperty(this.compatResultItemIdKey(), resultItem);
        resultJson.addProperty("count", resultCount);
        recipeJson.add("result", resultJson);
        return recipeJson;
    }

    /** 以配方 ID 为键写入配方映射。 */
    @Override
    public void addToRecipeMap(Map<Identifier, JsonElement> recipeMap) {
        recipeMap.put(recipeId, toJson());
    }
}
