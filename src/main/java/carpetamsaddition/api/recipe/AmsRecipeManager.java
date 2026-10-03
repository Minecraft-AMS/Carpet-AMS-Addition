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

package carpetamsaddition.api.recipe;

import carpetamsaddition.CarpetAMSAdditionCustomRecipes;
import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.settings.AmsRuleMetadata;
import carpetamsaddition.utils.MinecraftServerUtil;

import com.google.gson.JsonElement;
//#if MC>12006
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
//#endif
//#if MC>=12002
import net.minecraft.world.item.crafting.RecipeHolder;
//#else
//$$ import net.minecraft.world.item.crafting.Recipe;
//#endif
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
//#if MC>=260300
//$$ import net.minecraft.resources.FileToIdConverter;
//$$ import net.minecraft.server.packs.PackResources;
//$$ import net.minecraft.server.packs.resources.Resource;
//$$ import net.minecraft.server.packs.resources.ResourceManager;
//#endif

//#if MC>12006
import org.jetbrains.annotations.NotNull;
//#endif

//#if MC>=260300
//$$ import java.io.ByteArrayInputStream;
//$$ import java.nio.charset.StandardCharsets;
//#endif
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义配方管理器。
 * 职责：
 * 1. 将 {@link AmsRecipeBuilder} 收集的配方模板统一转换为服务端配方 JSON 并注入配方表；
 * 2. 监听配方相关规则变化，异步重载服务器资源使新配方生效；
 * 3. 在玩家登录或重载完成后，自动补发玩家缺失的本模组配方。
 * 通过 reloadInProgress / reloadRequested 两个标志位合并并发的重载请求，避免重复重载。
 */
public final class AmsRecipeManager {
    private static final String MOD_ID = CarpetAMSAdditionServer.compactName;
    /** 是否有重载正在进行中 */
    private static boolean reloadInProgress;
    /** 重载期间是否又有新的重载请求（用于重载完成后再次触发） */
    private static boolean reloadRequested;

    private AmsRecipeManager() {}

    /**
     * 汇总当前构建器中的全部配方模板，生成 "配方 ID -> 配方 JSON" 映射。
     * 依次收集无序合成、有序合成、烧炼三种配方。
     */
    private static Map<Identifier, JsonElement> createRecipeJsonMap() {
        Map<Identifier, JsonElement> recipeMap = new HashMap<>();
        AmsRecipeBuilder builder = AmsRecipeBuilder.getInstance();
        builder.getShapelessRecipeList().forEach(recipe -> recipe.addToRecipeMap(recipeMap));
        builder.getShapedRecipeList().forEach(recipe -> recipe.addToRecipeMap(recipeMap));
        builder.getSmeltingRecipeList().forEach(recipe -> recipe.addToRecipeMap(recipeMap));
        return recipeMap;
    }

    /** 清空旧配方模板列表，并重新调用自定义配方的构建方法生成最新一批配方。 */
    private static void rebuildCustomRecipes() {
        AmsRecipeBuilder builder = AmsRecipeBuilder.getInstance();
        builder.getShapedRecipeList().clear();
        builder.getShapelessRecipeList().clear();
        builder.getSmeltingRecipeList().clear();
        CarpetAMSAdditionCustomRecipes.getInstance().buildRecipes();
    }

    //#if MC<260300
    /**
     * 将全部自定义配方 JSON 注入到服务端配方映射中。
     */
    private static void injectRecipesToMap(
        //#if MC>12006
        Map<Identifier, Recipe<?>> map, HolderLookup.Provider wrapperLookup
        //#else
        //$$ Map<ResourceLocation, JsonElement> map
        //#endif
    ) {
        //#if MC>12006
        createRecipeJsonMap().forEach((id, json) -> addRecipe(map, wrapperLookup, id, json));
        //#else
        //$$ map.putAll(createRecipeJsonMap());
        //#endif
    }
    //#endif

    //#if MC<260300
    /**
     * 服务端注册自定义配方的入口。
     * 先重建配方模板，再将其注入到服务端配方映射中。
     */
    public static void registerCustomRecipes(
        //#if MC>12006
        Map<Identifier, Recipe<?>> map, HolderLookup.Provider wrapperLookup
        //#else
        //$$ Map<ResourceLocation, JsonElement> map
        //#endif
    ) {
        rebuildCustomRecipes();
        //#if MC>12006
        injectRecipesToMap(map, wrapperLookup);
        //#else
        //$$ injectRecipesToMap(map);
        //#endif
    }
    //#else
    //$$ public static Map<Identifier, Resource> registerCustomRecipes(FileToIdConverter converter, ResourceManager resourceManager, Map<Identifier, Resource> original) {
    //$$     rebuildCustomRecipes();
    //$$     Map<Identifier, JsonElement> customRecipes = createRecipeJsonMap();
    //$$     if (customRecipes.isEmpty()) {
    //$$         return original;
    //$$     }
    //$$     Map<Identifier, Resource> recipes = new HashMap<>(original);
    //$$     try (PackResources source = recipes.values().stream().findFirst().map(Resource::source).orElseGet(() -> resourceManager.listPacks().findFirst().orElseThrow())) {
    //$$         customRecipes.forEach(
    //$$             (id, json) -> recipes.put(converter.idToFile(id),
    //$$             new Resource(source, () -> new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8))))
    //$$         );
    //$$     }
    //$$     return recipes;
    //$$ }
    //#endif

    //#if MC>12006
    /** 将单条配方 JSON 反序列化为配方对象后写入目标映射。 */
    private static void addRecipe(Map<Identifier, Recipe<?>> map, HolderLookup.Provider wrapperLookup, Identifier id, JsonElement json) {
        RecipeHolder<?> recipeEntry = deserializeRecipe(ResourceKey.create(Registries.RECIPE, id), json.getAsJsonObject(), wrapperLookup);
        map.put(id, recipeEntry.value());
    }
    //#endif

    //#if MC>12006
    /** 使用配方编解码器将 JSON 解析为配方并包装为 RecipeHolder。 */
    @SuppressWarnings("RedundantCast")
    private static RecipeHolder<?> deserializeRecipe(ResourceKey<@NotNull Recipe<?>> key, JsonObject json, HolderLookup.Provider registries) {
        Recipe<?> recipe =
            //#if MC>=260300
            //$$ (Recipe<?>) Recipe.DIRECT_CODEC
            //#else
            (Recipe<?>) Recipe.CODEC
            //#endif
            .parse(registries.createSerializationContext(JsonOps.INSTANCE), json).getOrThrow(JsonParseException::new);
        return new RecipeHolder<>(key, recipe);
    }
    //#endif

    /**
     * 玩家登录回调：服务器运行中且存在激活的配方规则时，为该玩家补发缺失的本模组配方。
     */
    public static void onPlayerLoggedIn(MinecraftServer server, ServerPlayer player) {
        if (!MinecraftServerUtil.serverIsRunning(server) || !AmsRuleMetadata.hasActiveRecipeRule()) {
            return;
        }

        awardMissingRecipes(player, getAmsRecipes(server));
    }

    /**
     * 重载服务器资源（供配方规则变化后调用）。
     * 通过 {@code server.execute} 将实际重载调度到服务器主线程执行。
     */
    public static void reloadServerResources(MinecraftServer server) {
        if (!MinecraftServerUtil.serverIsRunning(server)) {
            return;
        }

        server.execute(() -> queueReload(server));
    }

    /**
     * 排队一次配方重载。
     * 若当前没有重载在进行，立即开始；否则只记录请求，待当前重载结束后自动再次触发。
     */
    private static void queueReload(MinecraftServer server) {
        reloadRequested = true;

        if (!reloadInProgress) {
            reloadRecipes(server);
        }
    }

    /** 执行资源重载：重建配方模板并调用服务器资源重载，完成后回到主线程执行收尾。 */
    private static void reloadRecipes(MinecraftServer server) {
        reloadRequested = false;
        reloadInProgress = true;
        rebuildCustomRecipes();

        server.reloadResources(server.getPackRepository().getSelectedIds()).whenComplete(
            (unused, throwable) -> server.execute(() -> finishReload(server, throwable))
        );
    }

    /**
     * 重载收尾：清除进行中标记；失败则记录错误日志，
     * 若期间又有新的重载请求则再次重载，否则成功时向全体在线玩家补发缺失配方。
     */
    private static void finishReload(MinecraftServer server, Throwable throwable) {
        reloadInProgress = false;
        if (throwable != null) {
            CarpetAMSAdditionServer.LOGGER.error("Failed to reload server resources after recipe rule change", throwable);
        }

        if (reloadRequested) {
            reloadRecipes(server);
        } else if (throwable == null) {
            awardMissingRecipes(server);
        }
    }

    /** 为全体在线玩家补发缺失的本模组配方。 */
    private static void awardMissingRecipes(MinecraftServer server) {
        List<Object> recipes = getAmsRecipes(server);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            awardMissingRecipes(player, recipes);
        }
    }

    /** 为单个玩家补发其配方书中缺失的配方。 */
    private static void awardMissingRecipes(ServerPlayer player, List<Object> recipes) {
        List<Object> missingRecipes = new ArrayList<>();

        for (Object recipe : recipes) {
            if (!hasRecipe(player, recipe)) {
                missingRecipes.add(recipe);
            }
        }

        if (!missingRecipes.isEmpty()) {
            awardRecipes(player, missingRecipes);
        }
    }

    /** 收集服务端配方表中所有命名空间属于本模组的配方。 */
    private static List<Object> getAmsRecipes(MinecraftServer server) {
        List<Object> recipes = new ArrayList<>();

        for (Object recipe : server.getRecipeManager().getRecipes()) {
            if (MOD_ID.equals(getRecipeIdentifier(recipe).getNamespace())) {
                recipes.add(recipe);
            }
        }

        return recipes;
    }

    /** 兼容不同版本，取出配方对象的唯一标识（ID）。 */
    private static Identifier getRecipeIdentifier(Object recipe) {
        //#if MC>12006
        return ((RecipeHolder<?>) recipe).id().identifier();
        //#elseif MC>=12002
        //$$ return ((RecipeHolder<?>) recipe).id();
        //#else
        //$$ return ((Recipe<?>) recipe).getId();
        //#endif
    }

    /** 判断玩家的配方书中是否已包含指定配方。 */
    private static boolean hasRecipe(ServerPlayer player, Object recipe) {
        //#if MC>=12002
        return player.getRecipeBook().contains(((RecipeHolder<?>) recipe).id());
        //#else
        //$$ return player.getRecipeBook().contains(((Recipe<?>) recipe).getId());
        //#endif
    }

    /** 将配方列表批量授予玩家（写入其配方书）。 */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void awardRecipes(ServerPlayer player, List<Object> recipes) {
        player.awardRecipes((List) recipes);
    }
}
