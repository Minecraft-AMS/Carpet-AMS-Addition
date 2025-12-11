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

import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class RecipeRuleHelper {
    private static final String MOD_ID = AmsServer.compactName;

    public static void onPlayerLoggedIn(MinecraftServer server, ServerPlayer player) {
        if (server != null && server.isRunning() && hasActiveRecipeRule()) {
            Collection<RecipeHolder<?>> allRecipes = getServerRecipeManager(server).getRecipes();
            for (RecipeHolder<?> recipe : allRecipes) {
                if (recipe.id().identifier().getNamespace().equals(MOD_ID) && !player.getRecipeBook().contains(recipe.id())) {
                    player.awardRecipes(List.of(recipe));
                }
            }
        }
    }

    public static void onValueChange(MinecraftServer server) {
        if (server != null && server.isRunning()) {
            server.execute(() -> {
            AmsRecipeManager.clearRecipeListMemory(AmsRecipeBuilder.getInstance());
            AmsServerCustomRecipes.getInstance().buildRecipes();
                needReloadServerResources(server);
                Collection<RecipeHolder<?>> allRecipes = getServerRecipeManager(server).getRecipes();
                for (RecipeHolder<?> recipe : allRecipes) {
                    if (recipe.id().identifier().getNamespace().equals(MOD_ID)) {
                        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                            if (!player.getRecipeBook().contains(recipe.id())) {
                                player.awardRecipes(List.of(recipe));
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
        server.reloadResources(server.getPackRepository().getSelectedIds());
    }
}
