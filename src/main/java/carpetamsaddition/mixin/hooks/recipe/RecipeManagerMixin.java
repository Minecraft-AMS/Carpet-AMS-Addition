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

//#if MC>12006
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.SortedMap;
//#else
//$$ import com.google.gson.JsonElement;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import net.minecraft.world.item.crafting.RecipeManager;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//$$ import java.util.Map;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.byteeeee.annotationtoolbox.annotation.GameVersion;

@GameVersion(version = "Minecraft < 26.3")
@Mixin(value = RecipeManager.class, priority = 16888)
public abstract class RecipeManagerMixin {
    //#if MC>12006
    @Inject(
        method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/ArrayList;<init>(I)V"
        )
    )
    private void addCustomRecipes(CallbackInfoReturnable<RecipeMap> cir, @Local SortedMap<Identifier, Recipe<?>> recipes) {
        CarpetAMSAdditionServer.getInstance().registerCustomRecipes(recipes, ((RecipeManagerAccessor) this).getRegistries());
    }
    //#else
    //$$ @ModifyVariable(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"), argsOnly = true)
    //$$ private Map<ResourceLocation, JsonElement> registerCustomRecipes(Map<ResourceLocation, JsonElement> map) {
    //$$     CarpetAMSAdditionServer.getInstance().registerCustomRecipes(map);
    //$$     return map;
    //$$ }
    //#endif
}
