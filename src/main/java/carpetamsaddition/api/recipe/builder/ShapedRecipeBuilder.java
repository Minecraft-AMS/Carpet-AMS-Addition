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

package carpetamsaddition.api.recipe.builder;

import carpetamsaddition.api.recipe.AmsRecipeBuilder;
import carpetamsaddition.utils.ChainableHashMap;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有序合成（工作台）配方构建器。
 * 通过链式 API 指定 3 字符宽的行图案与字符到物品的映射，
 * 最终在 {@link #build()} 时转换为 ShapedRecipeTemplate 注册到 {@link AmsRecipeBuilder}。
 */
public class ShapedRecipeBuilder extends AbstractRecipeBuilder {
    private final List<String> patternRows = new ArrayList<>();
    private final Map<Character, Item> ingredients = new HashMap<>();

    private ShapedRecipeBuilder(boolean enabled, String recipeName) {
        super(enabled, recipeName);
    }

    /** 创建有序合成配方构建器。 */
    public static ShapedRecipeBuilder create(boolean enabled, String recipeName) {
        return new ShapedRecipeBuilder(enabled, recipeName);
    }

    /**
     * 追加一行合成图案。
     *
     * @param row 每行必须恰好 3 个字符，否则抛出 {@link IllegalArgumentException}
     * @return 当前构建器本身
     */
    public ShapedRecipeBuilder pattern(String row) {
        if (row.length() != 3) {
            throw new IllegalArgumentException("Pattern row must be 3 characters");
        }
        patternRows.add(row);
        return this;
    }

    /** 将图案中的某个字符绑定到具体物品。 */
    public ShapedRecipeBuilder define(char symbol, Item item) {
        ingredients.put(symbol, item);
        return this;
    }

    /** 构建并注册配方：未启用或未设置产物时跳过，将图案行拆为字符数组并转换为注册名后提交。 */
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
