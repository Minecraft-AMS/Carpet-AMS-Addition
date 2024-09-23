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
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.api.recipe.AmsRecipeManager;
import club.mcams.carpet.api.recipe.AmsRecipeRegistry;
import club.mcams.carpet.settings.RecipeRule;

import net.minecraft.recipe.Recipe;
//#if MC>=12002
//$$ import net.minecraft.recipe.RecipeEntry;
//#endif
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.Collection;

public class RecipeRuleHelper {
    public static void onPlayerLoggedIn(MinecraftServer server, ServerPlayerEntity player) {
        if (server != null && !server.isStopping() && !server.isStopped() && hasActiveRecipeRule()) {
            RecipeManager recipeManager = server.getRecipeManager();
            //#if MC>=12002
            //$$ Collection<RecipeEntry<?>> allRecipes = recipeManager.values();
            //$$ for (RecipeEntry<?> recipe : allRecipes) {
            //$$     if (recipe.id().getNamespace().equals(AmsServer.compactName) && !player.getRecipeBook().contains(recipe)) {
            //$$         server.getCommandManager().executeWithPrefix(
            //$$             server.getCommandSource().withSilent(),
            //$$             String.format("/recipe give %s %s", player.getName().getString(), recipe)
            //$$         );
            //$$     }
            //$$ }
            //#else
            Collection<Recipe<?>> allRecipes = recipeManager.values();
            for (Recipe<?> recipe : allRecipes) {
                Identifier recipeId = recipe.getId();
                if (recipeId.getNamespace().equals(AmsServer.compactName) && !player.getRecipeBook().contains(recipeId)) {
                    server.getCommandManager().execute(
                        server.getCommandSource().withSilent(),
                        String.format("/recipe give %s %s", player.getName().getString(), recipeId)
                    );
                }
            }
            //#endif
        }
    }

    public static void onValueChange(MinecraftServer server) {
        if (server != null && !server.isStopping() && !server.isStopped()) {
            AmsRecipeManager.clearRecipeListMemory(AmsRecipeRegistry.getInstance());
            AmsRecipeRegistry.getInstance().register();
            server.execute(() -> {
                server.getCommandManager().execute(server.getCommandSource().withSilent(), "/reload");
                RecipeManager recipeManager = server.getRecipeManager();
                //#if MC>=12002
                //$$ Collection<RecipeEntry<?>> allRecipes = recipeManager.values();
                //$$ for (RecipeEntry<?> recipe : allRecipes) {
                //$$     if (recipe.id().getNamespace().equals(AmsServer.compactName)) {
                //$$         server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(), "/recipe give @a " + recipe);
                //$$     }
                //$$ }
                //#else
                Collection<Recipe<?>> allRecipes = recipeManager.values();
                for (Recipe<?> recipe : allRecipes) {
                    Identifier recipeId = recipe.getId();
                    if (recipeId.getNamespace().equals(AmsServer.compactName)) {
                        server.getCommandManager().execute(server.getCommandSource().withSilent(), "/recipe give @a " + recipeId);
                    }
                }
                //#endif
            });
        }
    }

    public static void onServerLoadedWorlds(MinecraftServer server) {
        if (server != null && !server.isStopping() && !server.isStopped() && hasActiveRecipeRule()) {
            server.execute(() -> {
                CommandManager commandManager = server.getCommandManager();
                commandManager.execute(server.getCommandSource(), "/reload");
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
}
