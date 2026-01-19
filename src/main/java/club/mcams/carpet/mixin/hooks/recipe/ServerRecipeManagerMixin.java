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

package club.mcams.carpet.mixin.hooks.recipe;

import club.mcams.carpet.AmsServer;

import com.google.gson.JsonElement;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.*;

@GameVersion(version = "Minecraft < 1.21.2")
@Mixin(value = RecipeManager.class, priority = 16888)
public abstract class ServerRecipeManagerMixin {
    @ModifyVariable(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"), argsOnly = true)
    private Map<Identifier, JsonElement> registerCustomRecipes(Map<Identifier, JsonElement> map) {
        AmsServer.getInstance().registerCustomRecipes(map);
        return map;
    }
}
