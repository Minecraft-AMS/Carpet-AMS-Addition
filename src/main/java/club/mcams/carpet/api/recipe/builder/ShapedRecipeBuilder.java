/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package club.mcams.carpet.api.recipe.builder;

import club.mcams.carpet.api.recipe.AmsRecipeBuilder;
import club.mcams.carpet.utils.ChainableHashMap;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapedRecipeBuilder extends AbstractRecipeBuilder {
    private final List<String> patternRows = new ArrayList<>();
    private final Map<Character, Item> ingredients = new HashMap<>();

    private ShapedRecipeBuilder(boolean enabled, String recipeName) {
        super(enabled, recipeName);
    }

    public static ShapedRecipeBuilder create(boolean enabled, String recipeName) {
        return new ShapedRecipeBuilder(enabled, recipeName);
    }

    public ShapedRecipeBuilder pattern(String row) {
        if (row.length() != 3) {
            throw new IllegalArgumentException("Pattern row must be 3 characters");
        }
        patternRows.add(row);
        return this;
    }

    public ShapedRecipeBuilder define(char symbol, Item item) {
        ingredients.put(symbol, item);
        return this;
    }

    @Override
    public void build() {
        if (!enabled || resultItem == null) {
            return;
        }
        String[][] pattern = new String[patternRows.size()][];
        for (int i = 0; i < patternRows.size(); i++) {
            pattern[i] = patternRows.get(i).split("");
        }
        ChainableHashMap<Character, String> ingredientMap = new ChainableHashMap<>();
        ingredients.forEach((k, v) -> ingredientMap.cPut(k, item(v)));
        AmsRecipeBuilder.getInstance().addShapedRecipe(recipeName, pattern, ingredientMap, item(resultItem), resultCount);
    }
}
