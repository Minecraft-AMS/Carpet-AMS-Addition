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

import net.minecraft.recipe.Recipe;
//#if MC>=12002
//$$ import net.minecraft.recipe.RecipeEntry;
//#endif

//#if MC>=12102
//$$ import net.minecraft.recipe.ServerRecipeManager;
//#else
import net.minecraft.recipe.RecipeManager;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.Collection;

public class RecipeRuleHelper {
    public static void onPlayerLoggedIn(MinecraftServer server, ServerPlayerEntity player) {
        if (server != null && server.isRunning() && hasActiveRecipeRule()) {
            //#if MC>=12002
            //$$ Collection<RecipeEntry<?>> allRecipes = getServerRecipeManager(server).values();
            //$$ for (RecipeEntry<?> recipe : allRecipes) {
            //$$     Identifier recipeId = getRecipeId(recipe);
            //$$     if (recipeId.getNamespace().startsWith(AmsServer.compactName)) {
            //$$         server.getCommandManager().executeWithPrefix(
            //$$             server.getCommandSource().withSilent(),
            //$$             String.format("/recipe give %s %s", player.getName().getString(), recipeId)
            //$$         );
            //$$     }
            //$$ }
            //#else
            Collection<Recipe<?>> allRecipes = getServerRecipeManager(server).values();
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
        if (server != null && server.isRunning()) {
            AmsRecipeManager.clearRecipeListMemory(AmsRecipeBuilder.getInstance());
            AmsServerCustomRecipes.getInstance().buildRecipes();
            server.execute(() -> {
                server.getCommandManager().execute(server.getCommandSource().withSilent(), "/reload");
                //#if MC>=12002
                //$$ Collection<RecipeEntry<?>> allRecipes = getServerRecipeManager(server).values();
                //$$ for (RecipeEntry<?> recipe : allRecipes) {
                //$$     Identifier recipeId = getRecipeId(recipe);
                //$$     if (recipeId.getNamespace().startsWith(AmsServer.compactName)) {
                //$$         server.getCommandManager().executeWithPrefix(
                //$$             server.getCommandSource().withSilent(),
                //$$             "/recipe give @a " + recipeId.toString()
                //$$         );
                //$$     }
                //$$ }
                //#else
                Collection<Recipe<?>> allRecipes = getServerRecipeManager(server).values();
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
        if (server != null && server.isRunning() && hasActiveRecipeRule()) {
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

    //#if MC>=12102
    //$$ private static ServerRecipeManager getServerRecipeManager(MinecraftServer server) {
    //#else
    private static RecipeManager getServerRecipeManager(MinecraftServer server) {
    //#endif
        return server.getRecipeManager();
    }

    //#if MC>=12102
    //$$ private static Identifier getRecipeId(RecipeEntry<?> recipe) {
    //$$     return recipe.id().getValue();
    //$$ }
    //#elseif MC>=12002
    //$$ private static Identifier getRecipeId(RecipeEntry<?> recipe) {
    //$$     return recipe.id();
    //$$ }
    //#endif
}
