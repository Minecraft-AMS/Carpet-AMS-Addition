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

import net.minecraft.recipe.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@GameVersion(version = "Minecraft >= 1.21.2")
@Mixin(ServerRecipeManager.class)
public abstract class RecipeManagerMixin {

    @Unique
    private RegistryWrapper.WrapperLookup wrapperLookup;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
        this.wrapperLookup = registries;
    }

    @SuppressWarnings("DuplicatedCode")
    @Inject(method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/recipe/PreparedRecipes;", at = @At("RETURN"), cancellable = true)
    private void addCustomRecipes(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<PreparedRecipes> cir) {
        SortedMap<Identifier, Recipe<?>> sortedMap = new TreeMap<>();
        SortedMap<Identifier, Recipe<?>> originalMap = cir.getReturnValue().recipes().stream().collect(Collectors.toMap(recipeEntry -> recipeEntry.id().getValue(), RecipeEntry::value, (a, b) -> a, TreeMap::new));
        sortedMap.putAll(originalMap);
        AmsServer.getInstance().registerCustomRecipes(sortedMap, wrapperLookup);
        List<RecipeEntry<?>> list = new ArrayList<>(sortedMap.size());
        sortedMap.forEach((id, recipe) -> {
            RegistryKey<Recipe<?>> registryKey = RegistryKey.of(RegistryKeys.RECIPE, id);
            RecipeEntry<?> recipeEntry = new RecipeEntry<>(registryKey, recipe);
            list.add(recipeEntry);
        });
        cir.setReturnValue(PreparedRecipes.of(list));
    }
}
