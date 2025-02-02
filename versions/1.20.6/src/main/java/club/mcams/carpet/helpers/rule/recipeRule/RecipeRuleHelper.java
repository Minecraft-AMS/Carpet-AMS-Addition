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

package club.mcams.carpet.helpers.rule.recipeRule;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.AmsServerCustomRecipes;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.api.recipe.AmsRecipeBuilder;
import club.mcams.carpet.api.recipe.AmsRecipeManager;
import club.mcams.carpet.settings.RecipeRule;

import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
@GameVersion(version = "Minecraft >= 1.20.2")
public class RecipeRuleHelper {
    public static void onPlayerLoggedIn(MinecraftServer server, ServerPlayerEntity player) {
        if (server != null && server.isRunning() && hasActiveRecipeRule()) {
            Collection<RecipeEntry<?>> allRecipes = getServerRecipeManager(server).values();
            for (RecipeEntry<?> recipe : allRecipes) {
                if (recipe.id().getNamespace().equals(AmsServer.compactName) && !player.getRecipeBook().contains(recipe.id())) {
                    player.unlockRecipes(List.of(recipe));
                }
            }
        }
    }

    public static void onValueChange(MinecraftServer server) {
        if (server != null && server.isRunning()) {
            AmsRecipeManager.clearRecipeListMemory(AmsRecipeBuilder.getInstance());
            AmsServerCustomRecipes.getInstance().buildRecipes();
            server.execute(() -> {
                needReloadServerResources(server);
                Collection<RecipeEntry<?>> allRecipes = getServerRecipeManager(server).values();
                for (RecipeEntry<?> recipe : allRecipes) {
                    if (recipe.id().getNamespace().equals(AmsServer.compactName)) {
                        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                            if (!player.getRecipeBook().contains(recipe.id())) {
                                player.unlockRecipes(List.of(recipe));
                            }
                        }
                    }
                }
            });
        }
    }

    private static boolean hasActiveRecipeRule() {
        Field[] fields = AmsServerSettings.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RecipeRule.class)) {
                try {
                    field.setAccessible(true);
                    if (field.getBoolean(null)) {
                        return true;
                    }
                } catch (IllegalAccessException e) {
                    AmsServer.LOGGER.warn("Failed to access RecipeRule field: {}", field.getName(), e);
                }
            }
        }
        return false;
    }

    private static RecipeManager getServerRecipeManager(MinecraftServer server) {
        return server.getRecipeManager();
    }

    public static void needReloadServerResources(MinecraftServer server) {
        if (server != null && server.isRunning() && hasActiveRecipeRule()) {
            server.reloadResources(server.getDataPackManager().getEnabledIds());
        }
    }
}
