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

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.SortedMap;

@GameVersion(version = "Minecraft >= 1.21.2")
@Mixin(value = ServerRecipeManager.class, priority = 16888)
public abstract class ServerRecipeManagerMixin {
    @Inject(
        method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/recipe/PreparedRecipes;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/ArrayList;<init>(I)V"
        )
    )
    protected void prepare(CallbackInfoReturnable<PreparedRecipes> cir, @Local SortedMap<Identifier, Recipe<?>> sortedMap) {
        AmsServer.getInstance().registerCustomRecipes(sortedMap, ((ServerRecipeManagerAccessor) this).getRegistries());
    }
}
