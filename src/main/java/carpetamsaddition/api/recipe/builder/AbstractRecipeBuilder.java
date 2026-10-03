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

import carpetamsaddition.utils.RegexTools;

import net.minecraft.world.item.Item;

/**
 * 配方构建器的抽象基类。
 * 统一承载三种配方共有的配置：是否启用、配方名称、产物物品与数量；
 * 提供链式的 {@link #output(Item, int)} API 与将物品转为注册名的通用工具方法。
 */
public abstract class AbstractRecipeBuilder {
    protected final boolean enabled;
    protected final String recipeName;
    protected Item resultItem;
    protected int resultCount;

    protected AbstractRecipeBuilder(boolean enabled, String recipeName) {
        this.enabled = enabled;
        this.recipeName = recipeName;
    }

    /**
     * 链式设置产物物品与数量。
     *
     * @return 当前构建器本身，便于继续链式调用
     */
    public AbstractRecipeBuilder output(Item item, int count) {
        this.resultItem = item;
        this.resultCount = count;
        return this;
    }

    /** 将物品转换为注册名（供生成配方 JSON 时使用） */
    protected String item(Item item) {
        return RegexTools.getItemRegisterName(item.getDefaultInstance());
    }

    /** 由子类实现具体配方类型的构建逻辑（向 {@code AmsRecipeBuilder} 注册模板）。*/
    public abstract void build();
}
