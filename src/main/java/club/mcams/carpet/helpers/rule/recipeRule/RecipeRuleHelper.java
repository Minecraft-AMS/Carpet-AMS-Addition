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
import club.mcams.carpet.api.recipe.AmsRecipeManager;
import club.mcams.carpet.api.recipe.AmsRecipeBuilder;
import club.mcams.carpet.settings.RecipeRule;
import club.mcams.carpet.utils.MinecraftServerUtil;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@GameVersion(version = "Minecraft < 1.20.2")
public class RecipeRuleHelper {
    private static final String MOD_ID = AmsServer.compactName;

    public static void onPlayerLoggedIn(MinecraftServer server, ServerPlayerEntity player) {
        if (MinecraftServerUtil.serverIsRunning(server) && hasActiveRecipeRule()) {
            Collection<Recipe<?>> allRecipes = getServerRecipeManager(server).values();
            for (Recipe<?> recipe : allRecipes) {
                if (recipe.getId().getNamespace().equals(MOD_ID) && !player.getRecipeBook().contains(recipe.getId())) {
                    player.unlockRecipes(List.of(recipe));
                }
            }
        }
    }

    public static void onValueChange(MinecraftServer server) {
        if (MinecraftServerUtil.serverIsRunning(server)) {
            AmsRecipeManager.clearRecipeListMemory(AmsRecipeBuilder.getInstance());
            AmsServerCustomRecipes.getInstance().buildRecipes();
            server.execute(() -> {
                needReloadServerResources(server);
                Collection<Recipe<?>> allRecipes = getServerRecipeManager(server).values();
                for (Recipe<?> recipe : allRecipes) {
                    if (recipe.getId().getNamespace().equals(MOD_ID)) {
                        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                            if (!player.getRecipeBook().contains(recipe.getId())) {
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
        if (MinecraftServerUtil.serverIsRunning(server) && hasActiveRecipeRule()) {
            server.reloadResources(server.getDataPackManager().getEnabledNames());
        }
    }
}
