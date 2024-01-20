/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;

import club.mcams.carpet.commands.RegisterCommands;
import club.mcams.carpet.config.LoadConfigFromJson;
import club.mcams.carpet.logging.AmsCarpetLoggerRegistry;
import club.mcams.carpet.settings.CarpetRuleRegistrar;
import club.mcams.carpet.translations.AMSTranslations;
import club.mcams.carpet.translations.TranslationConstants;
import club.mcams.carpet.utils.CountRulesUtil;
import club.mcams.carpet.utils.CraftingRuleUtil;

import com.google.common.collect.Maps;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.Map;

public class AmsServer implements CarpetExtension {
    public static MinecraftServer minecraftServer;
    public static final int ruleCount = CountRulesUtil.countRules();
    public static final String fancyName = "Carpet AMS Addition";
    public static final String name = AmsServerMod.getModId();
    public static final String compactName = name.replace("-","");  // carpetamsaddition
    public static final Logger LOGGER = LogManager.getLogger(fancyName);
    private static final AmsServer INSTANCE = new AmsServer();

    @Override
    public void onGameStarted() {
        // let's /carpet handle our few simple settings
        LOGGER.info(String.format("%s v%s loaded! (Total rules: %d)", fancyName, AmsServerMod.getVersion(), ruleCount));
        LOGGER.info("open source: https://github.com/Minecraft-AMS/Carpet-AMS-Addition");
        LOGGER.info("issues: https://github.com/Minecraft-AMS/Carpet-AMS-Addition/issues");
        CarpetRuleRegistrar.register(CarpetServer.settingsManager, AmsServerSettings.class);
    }

    @Override
    public String version() {
        return AmsServerMod.getModId();
    }

    public static void init() {
        CarpetServer.manageExtension(INSTANCE);
        AMSTranslations.loadTranslations();
    }

    @Override
    public void registerLoggers() {
        AmsCarpetLoggerRegistry.registerLoggers();
    }

    @Override
    public void registerCommands(
        CommandDispatcher<ServerCommandSource> dispatcher
        //#if MC>=11900
        //$$ , final CommandRegistryAccess commandBuildContext
        //#endif
    ) {
        RegisterCommands.registerCommands(
            dispatcher
            //#if MC>=11900
            //$$ , commandBuildContext
            //#endif
        );
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        Map<String, String> trimmedTranslation = Maps.newHashMap();
        String prefix = TranslationConstants.CARPET_TRANSLATIONS_KEY_PREFIX;
        AMSTranslations.getTranslation(lang).forEach((key, value) -> {
            if (key.startsWith(prefix)) {
                String newKey = key.substring(prefix.length());
                //#if MC>=11900
                //$$ newKey = "carpet." + newKey;
                //#endif
                trimmedTranslation.put(newKey, value);
            }
        });
        return trimmedTranslation;
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        minecraftServer = server;
        LoadConfigFromJson.load(server);
    }

    @Override
    public void onServerClosed(MinecraftServer minecraftServer) {
        CraftingRuleUtil.clearAmsDataDatapacks(minecraftServer);
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer minecraftServer) {
        CraftingRuleUtil.loadAmsDataDatapacks(minecraftServer);
    }
}