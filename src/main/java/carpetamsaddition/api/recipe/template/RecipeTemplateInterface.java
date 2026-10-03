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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.Identifier;

import java.util.Map;

/**
 * 配方模板统一接口。
 * 每种配方模板实现该接口后，即可将自身转为配方 JSON 并写入配方映射，
 * 供 {@code AmsRecipeManager} 统一注入服务端配方表。
 */
public interface RecipeTemplateInterface {
    /** 将本模板转换为配方 JSON 对象 */
    JsonObject toJson();

    /** 以配方 ID 为键，将本模板对应的配方 JSON 写入目标映射 */
    void addToRecipeMap(Map<Identifier, JsonElement> recipeMap);

    /**
     * 兼容不同版本返回产物物品 ID 的 JSON 键名：
     */
    default String compatResultItemIdKey() {
        //#if MC>=12005
        //$$ return "id";
        //#else
        return "item";
        //#endif
    }
}
