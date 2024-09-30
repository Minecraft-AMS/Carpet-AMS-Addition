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

package club.mcams.carpet.mixin.hooks.RecipeRule;

import club.mcams.carpet.AmsServer;

import com.google.gson.JsonElement;

//#if MC>=12102
//$$ import net.minecraft.recipe.Recipe;
//$$ import net.minecraft.registry.RegistryWrapper;
//#endif
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import org.spongepowered.asm.mixin.Mixin;
//#if MC>=12102
//$$ import org.spongepowered.asm.mixin.Unique;
//#endif
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    //#if MC>=12102
    //$$ @Unique
    //$$ private RegistryWrapper.WrapperLookup wrapperLookup;
    //#endif

    //#if MC>=12102
    //$$ @Inject(method = "<init>", at = @At("TAIL"))
    //$$ private void init(RegistryWrapper.WrapperLookup registries, CallbackInfo ci) {
    //$$     this.wrapperLookup = registries;
    //$$ }
    //#endif

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
    private void registerCustomRecipes(
        //#if MC>=12102
        //$$ Map<Identifier, Recipe<?>> map,
        //#else
        Map<Identifier, JsonElement> map,
        //#endif
        ResourceManager resourceManager, Profiler profiler, CallbackInfo ci
    ) {
        //#if MC>=12102
        //$$ AmsServer.getInstance().registerCustomRecipes(map, this.wrapperLookup);
        //#else
        AmsServer.getInstance().registerCustomRecipes(map);
        //#endif
    }
}
