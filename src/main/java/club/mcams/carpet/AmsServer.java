
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.bundled.BundledModule;
import carpet.settings.ParsedRule;
import club.mcams.carpet.command.AmsCarpetCommandRegistry;
import club.mcams.carpet.function.ChunkLoading;
import club.mcams.carpet.logging.AmsCarpetLoggerRegistry;
import club.mcams.carpet.util.AmsCarpetTranslations;
import club.mcams.carpet.util.AutoMixinAuditExecutor;
import club.mcams.carpet.util.JsonHelper;
import club.mcams.carpet.util.Logging;
import club.mcams.carpet.util.recipes.CraftingRule;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
//import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AmsServer implements CarpetExtension, ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("Ams");
    private static MinecraftServer minecraftServer;

    @Override
    public void registerLoggers() {
        AmsCarpetLoggerRegistry.registerLoggers();
    }


    //#if MC>=11900
    //$$    @Override
    //$$    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess commandBuildContext) {
    //$$        AmsCarpetCommandRegistry.register(dispatcher);
    //$$    }
    //#else
    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        AmsCarpetCommandRegistry.register(dispatcher);
    }
    //#endif

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player) {
        ChunkLoading.onPlayerConnect(player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player) {
        ChunkLoading.onPlayerDisconnect(player);
    }

    @Override
    public String version() {
        return "carpet-ams-addition";
    }

    public static void loadExtension() {
        CarpetServer.manageExtension(new AmsServer());
    }

    @Override
    public void onInitialize() {
        AmsServer.loadExtension();
        AutoMixinAuditExecutor.run();
    }

    @Override
    public void onGameStarted() {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(AmsServerSettings.class);
        LOGGER.info("Carpet-AMS-Addition Loaded!");
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return AmsCarpetTranslations.getTranslationFromResourcePath(lang);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        File datapackPath = new File(server.getSavePath(WorldSavePath.DATAPACKS).toString() + "/AmsData/data/");
        if (Files.isDirectory(datapackPath.toPath())) {
            try {
                FileUtils.deleteDirectory(datapackPath);
            } catch (IOException e) {
                Logging.logStackTrace(e);
            }
        }
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        minecraftServer = server;
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        String datapackPath = server.getSavePath(WorldSavePath.DATAPACKS).toString();
        if (Files.isDirectory(new File(datapackPath + "/Ams_flexibleData/").toPath())) {
            try {
                FileUtils.deleteDirectory(new File(datapackPath + "/Ams_flexibleData/"));
            } catch (IOException e) {
                Logging.logStackTrace(e);
            }
        }
        datapackPath += "/AmsData/";
        boolean isFirstLoad = !Files.isDirectory(new File(datapackPath).toPath());

        try {
            Files.createDirectories(new File(datapackPath + "data/ams/recipes").toPath());
            Files.createDirectories(new File(datapackPath + "data/ams/advancements").toPath());
            Files.createDirectories(new File(datapackPath + "data/minecraft/recipes").toPath());
            copyFile("assets/carpet-ams-addition/AmsRecipeTweakPack/pack.mcmeta", datapackPath + "pack.mcmeta");
        } catch (IOException e) {
            Logging.logStackTrace(e);
        }

        copyFile(
                "assets/carpet-ams-addition/AmsRecipeTweakPack/ams/advancements/root.json",
                datapackPath + "data/ams/advancements/root.json"
        );

        for (Field f : AmsServerSettings.class.getDeclaredFields()) {
            CraftingRule craftingRule = f.getAnnotation(CraftingRule.class);
            if (craftingRule == null) continue;
            registerCraftingRule(
                    craftingRule.name().isEmpty() ? f.getName() : craftingRule.name(),
                    craftingRule.recipes(),
                    craftingRule.recipeNamespace(),
                    datapackPath + "data/"
            );
        }
        reload();
        if (isFirstLoad) {
            server.getCommandManager().
                    //#if MC>=11900
                    //$$executeWithPrefix
                    //#else
                            execute
                    //#endif
                            (server.getCommandSource(),
                                    //#if MC<11900
                                    //$$"/"+
                                    //#endif
                                    "datapack enable \"file/AmsData\"");
        }
    }

    private void registerCraftingRule(String ruleName, String[] recipes, String recipeNamespace, String dataPath) {
        updateCraftingRule(
                CarpetServer.settingsManager.
                        //#if MC>=11900
                        //$$getCarpetRule(ruleName),
                        //#else
                                getRule(ruleName),
                //#endif
                recipes,
                recipeNamespace,
                dataPath,
                ruleName
        );

        CarpetServer.settingsManager.addRuleObserver((source, rule, s) -> {
            if (rule.
                    //#if MC>=11900
                    //$$name()
                    //#else
                            name
                    //#endif
                    .equals(ruleName)) {
                updateCraftingRule(rule, recipes, recipeNamespace, dataPath, ruleName);
                reload();
            }
        });
    }

    private void updateCraftingRule(
            ParsedRule<?> rule,
            String[] recipes,
            String recipeNamespace,
            String datapackPath,
            String ruleName
    ) {
        ruleName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, ruleName);
        //#if MC >= 11900
        //$$ if(rule.type() == String.class) {
        //$$    String value = rule.name();
        //#else
        if (rule.type == String.class) {
            String value = rule.getAsString();
        //#endif
            List<String> installedRecipes = Lists.newArrayList();
            try {
                Stream<Path> fileStream = Files.list(new File(datapackPath + recipeNamespace, "recipes").toPath());
                fileStream.forEach(( path -> {
                    for (String recipeName : recipes) {
                        String fileName = path.getFileName().toString();
                        if (fileName.startsWith(recipeName)) {
                            installedRecipes.add(fileName);
                        }
                    }
                } ));
                fileStream.close();
            } catch (IOException e) {
                Logging.logStackTrace(e);
            }

            deleteRecipes(installedRecipes.toArray(new String[0]), recipeNamespace, datapackPath, ruleName, false);

            if (recipeNamespace.equals("ams")) {
                List<String> installedAdvancements = Lists.newArrayList();
                try {
                    Stream<Path> fileStream = Files.list(new File(datapackPath, "ams/advancements").toPath());
                    String finalRuleName = ruleName;
                    fileStream.forEach(( path -> {
                        String fileName = path.getFileName().toString().replace(".json", "");
                        if (fileName.startsWith(finalRuleName)) {
                            installedAdvancements.add(fileName);
                        }
                    } ));
                    fileStream.close();
                } catch (IOException e) {
                    Logging.logStackTrace(e);
                }
                for (String advancement : installedAdvancements.toArray(new String[0])) {
                    removeAdvancement(datapackPath, advancement);
                }
            }

            if (!value.equals("off")) {
                List<String> tempRecipes = Lists.newArrayList();
                for (String recipeName : recipes) {
                    tempRecipes.add(recipeName + "_" + value + ".json");
                }

                copyRecipes(tempRecipes.toArray(new String[0]), recipeNamespace, datapackPath, ruleName + "_" + value);
            }
        } else if (
                //#if MC>=11900
                //$$ rule.type() == int.class
                //#else
                rule.type == int.class
                //#endif
                && (Integer) rule.get() > 0) {
            copyRecipes(recipes, recipeNamespace, datapackPath, ruleName);
            //#if MC>=11900
            //$$int value = (Integer)rule.value();
            //#else
            int value = (Integer) rule.get();
            //#endif
            for (String recipeName : recipes) {
                String filePath = datapackPath + recipeNamespace + "/recipes/" + recipeName;
                JsonObject jsonObject = JsonHelper.readJson(filePath);
                assert jsonObject != null;
                jsonObject.getAsJsonObject("result").addProperty("count", value);
                JsonHelper.writeJson(jsonObject, filePath);
            }
        } else if (
                //#if MC >= 11900
                //$$ rule.type() == boolean.class && (Boolean)rule.value()
                //#else
                rule.type == boolean.class && rule.getBoolValue()
                //#endif
                ) {
            copyRecipes(recipes, recipeNamespace, datapackPath, ruleName);
        } else {
            deleteRecipes(recipes, recipeNamespace, datapackPath, ruleName, true);
        }
    }

    private void copyRecipes(String[] recipes, String recipeNamespace, String datapackPath, String ruleName) {
        for (String recipeName : recipes) {
            copyFile(
                    "assets/carpet-ams-addition/AmsRecipeTweakPack/" + recipeNamespace + "/recipes/" + recipeName,
                    datapackPath + recipeNamespace + "/recipes/" + recipeName
            );
        }
        if (recipeNamespace.equals("ams")) {
            writeAdvancement(datapackPath, ruleName, recipes);
        }
    }

    private void deleteRecipes(
            String[] recipes,
            String recipeNamespace,
            String datapackPath,
            String ruleName,
            boolean removeAdvancement
    ) {
        for (String recipeName : recipes) {
            try {
                Files.deleteIfExists(new File(datapackPath + recipeNamespace + "/recipes", recipeName).toPath());
            } catch (IOException e) {
                Logging.logStackTrace(e);
            }
        }
        if (removeAdvancement && recipeNamespace.equals("ams")) {
            removeAdvancement(datapackPath, ruleName);
        }
    }

    private void writeAdvancement(String datapackPath, String ruleName, String[] recipes) {
        copyFile(
                "assets/carpet-ams-addition/AmsRecipeTweakPack/ams/advancements/recipe_rule.json",
                datapackPath + "ams/advancements/" + ruleName + ".json"
        );

        JsonObject advancementJson = JsonHelper.readJson(datapackPath + "ams/advancements/" + ruleName + ".json");
        assert advancementJson != null;
        JsonArray recipeRewards = advancementJson.getAsJsonObject("rewards").getAsJsonArray("recipes");

        for (String recipeName : recipes) {
            recipeRewards.add("ams:" + recipeName.replace(".json", ""));
        }
        JsonHelper.writeJson(advancementJson, datapackPath + "ams/advancements/" + ruleName + ".json");
    }

    private void removeAdvancement(String datapackPath, String ruleName) {
        try {
            Files.deleteIfExists(new File(datapackPath + "ams/advancements/" + ruleName + ".json").toPath());
        } catch (IOException e) {
            Logging.logStackTrace(e);
        }
    }

    private void reload() {
        ResourcePackManager resourcePackManager = minecraftServer.getDataPackManager();
        resourcePackManager.scanPacks();
        Collection<String> collection = Lists.newArrayList(resourcePackManager.getEnabledNames());
        collection.add("AmsData");

        ReloadCommand.tryReloadDataPacks(collection, minecraftServer.getCommandSource());
    }

    private void copyFile(String resourcePath, String targetPath) {
        InputStream source = BundledModule.class.getClassLoader().getResourceAsStream(resourcePath);
        Path target = new File(targetPath).toPath();

        try {
            assert source != null;
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Logging.logStackTrace(e);
        } catch (NullPointerException e) {
            LOGGER.error("Resource '" + resourcePath + "' is null:");
            Logging.logStackTrace(e);
        }
    }

//    public static void savePlayerData(ServerPlayerEntity player) {
//        File playerDataDir = minecraftServer.getSavePath(WorldSavePath.PLAYERDATA).toFile();
//        try {
//            NbtCompound compoundTag = player.writeNbt(new NbtCompound());
//            File file = File.createTempFile(player.getUuidAsString() + "-", ".dat", playerDataDir);
//            NbtIo.writeCompressed(compoundTag, file);
//            File file2 = new File(playerDataDir, player.getUuidAsString() + ".dat");
//            File file3 = new File(playerDataDir, player.getUuidAsString() + ".dat_old");
//            Util.backupAndReplace(file2, file, file3);
//        } catch (Exception ignored) {
//            LOGGER.warn("Failed to save player data for " + player.getName().getString());
//        }
//    }
}

