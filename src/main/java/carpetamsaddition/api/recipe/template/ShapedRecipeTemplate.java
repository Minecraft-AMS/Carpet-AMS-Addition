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

import java.util.Map;

/**
 * 有序合成配方模板。
 * 保存配方的图案（每行 3 字符）、字符到物品注册名的映射以及产物信息，
 * 可将其序列化为 minecraft:crafting_shaped 类型的配方 JSON。
 */
public class ShapedRecipeTemplate implements RecipeTemplateInterface {
    private final Identifier recipeId;
    private final String[][] pattern;
    private final Map<Character, String> ingredients;
    private final String resultItem;
    private final int resultCount;

    /**
     * @param recipeId    配方唯一标识
     * @param pattern     合成图案
     * @param ingredients 图案字符到物品注册名的映射
     * @param resultItem  产物物品注册名
     * @param resultCount 产物数量
     */
    public ShapedRecipeTemplate(Identifier recipeId, String[][] pattern, Map<Character, String> ingredients, String resultItem, int resultCount) {
        this.recipeId = recipeId;
        this.pattern = pattern;
        this.ingredients = ingredients;
        this.resultItem = resultItem;
        this.resultCount = resultCount;
    }

    /** 序列化为有序合成配方 JSON（pattern / key / result 结构）。 */
    @Override
    public JsonObject toJson() {
        JsonObject recipeJson = new JsonObject();
        recipeJson.addProperty("type", "minecraft:crafting_shaped");

        JsonArray patternJson = new JsonArray();
        for (String[] row : pattern) {
            StringBuilder rowString = new StringBuilder();
            for (String cell : row) {
                rowString.append(cell);
            }
            patternJson.add(rowString.toString());
        }
        recipeJson.add("pattern", patternJson);

        JsonObject keyJson = new JsonObject();

        for (Map.Entry<Character, String> entry : ingredients.entrySet()) {
            //#if MC>=12102
            keyJson.addProperty(entry.getKey().toString(), entry.getValue());
            //#else
            //$$ JsonObject ingredientJson = new JsonObject();
            //$$ ingredientJson.addProperty("item", entry.getValue());
            //$$ keyJson.add(entry.getKey().toString(), ingredientJson);
            //#endif
        }

        recipeJson.add("key", keyJson);

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
