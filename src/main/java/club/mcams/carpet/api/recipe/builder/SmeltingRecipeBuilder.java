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

import net.minecraft.item.Item;

public class SmeltingRecipeBuilder extends AbstractRecipeBuilder {
    private Item sourceItem;
    private float experience;
    private int cookingTime;

    private SmeltingRecipeBuilder(boolean enabled, String recipeName) {
        super(enabled, recipeName);
    }

    public static SmeltingRecipeBuilder create(boolean enabled, String recipeName) {
        return new SmeltingRecipeBuilder(enabled, recipeName);
    }

    public SmeltingRecipeBuilder material(Item item) {
        this.sourceItem = item;
        return this;
    }

    public SmeltingRecipeBuilder experience(float experience) {
        this.experience = experience;
        return this;
    }

    public SmeltingRecipeBuilder cookTime(int ticks) {
        this.cookingTime = ticks;
        return this;
    }

    @Override
    public void build() {
        if (!enabled || resultItem == null || sourceItem == null) {
            return;
        }
        AmsRecipeBuilder.getInstance().addSmeltingRecipe(recipeName, item(sourceItem), item(resultItem), experience, cookingTime);
    }
}
