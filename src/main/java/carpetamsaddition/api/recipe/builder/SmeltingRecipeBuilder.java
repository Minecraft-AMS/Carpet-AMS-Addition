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

import net.minecraft.world.item.Item;

/**
 * 烧炼配方构建器。
 * 通过链式 API 指定原料、产物、经验值与烧炼时长，
 * 最终在 {@link #build()} 时转换为 SmeltingRecipeTemplate 注册到 {@link AmsRecipeBuilder}。
 */
public class SmeltingRecipeBuilder extends AbstractRecipeBuilder {
    private Item material;
    private float experience;
    private int cookingTime;

    private SmeltingRecipeBuilder(boolean enabled, String recipeName) {
        super(enabled, recipeName);
    }

    /** 创建烧炼配方构建器。 */
    public static SmeltingRecipeBuilder create(boolean enabled, String recipeName) {
        return new SmeltingRecipeBuilder(enabled, recipeName);
    }

    /** 指定烧炼原料物品。 */
    public SmeltingRecipeBuilder material(Item item) {
        this.material = item;
        return this;
    }

    /** 指定烧炼产出的经验值。 */
    public SmeltingRecipeBuilder experience(float experience) {
        this.experience = experience;
        return this;
    }

    /** 指定烧炼所需时长（tick）。 */
    public SmeltingRecipeBuilder cookTime(int ticks) {
        this.cookingTime = ticks;
        return this;
    }

    /** 构建并注册配方：未启用、未设置产物或未设置原料时跳过。 */
    @Override
    public void build() {
        if (!enabled || resultItem == null || material == null) {
            return;
        }

        AmsRecipeBuilder.getInstance().addSmeltingRecipe(recipeName, item(material), item(resultItem), experience, cookingTime);
    }
}
