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

package carpetamsaddition.mixin.hooks.recipe;

import carpetamsaddition.CarpetAMSAdditionServer;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.ProfilerFiller;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Unique
    private HolderLookup.Provider wrapperLookup;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(HolderLookup.Provider registries, CallbackInfo ci) {
        this.wrapperLookup = registries;
    }

    @SuppressWarnings("DuplicatedCode")
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;", at = @At("RETURN"), cancellable = true)
    private void addCustomRecipes(ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfoReturnable<RecipeMap> cir) {
        SortedMap<Identifier, Recipe<?>> sortedMap = new TreeMap<>();
        SortedMap<Identifier, Recipe<?>> originalMap = cir.getReturnValue().values().stream().collect(Collectors.toMap(recipeEntry -> recipeEntry.id().identifier(), RecipeHolder::value, (a, b) -> a, TreeMap::new));
        sortedMap.putAll(originalMap);
        CarpetAMSAdditionServer.getInstance().registerCustomRecipes(sortedMap, wrapperLookup);
        List<RecipeHolder<?>> list = new ArrayList<>(sortedMap.size());
        sortedMap.forEach((id, recipe) -> {
            ResourceKey<@NotNull Recipe<?>> registryKey = ResourceKey.create(Registries.RECIPE, id);
            RecipeHolder<?> recipeEntry = new RecipeHolder<>(registryKey, recipe);
            list.add(recipeEntry);
        });
        cir.setReturnValue(RecipeMap.create(list));
    }
}
