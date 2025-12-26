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

package carpetamsaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;

import carpetamsaddition.api.recipe.AmsRecipeManager;
import carpetamsaddition.api.recipe.AmsRecipeBuilder;
import carpetamsaddition.commands.RegisterCommands;
import carpetamsaddition.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommandRegistry;
import carpetamsaddition.commands.rule.commandPlayerLeader.LeaderCommandRegistry;
import carpetamsaddition.commands.rule.commandSetPlayerPose.SetPlayerPoseCommandRegistry;
import carpetamsaddition.config.LoadConfigFromJson;
import carpetamsaddition.config.rule.welcomeMessage.CustomWelcomeMessageConfig;
import carpetamsaddition.helpers.rule.fancyFakePlayerName.FancyFakePlayerNameTeamController;
import carpetamsaddition.helpers.rule.recipeRule.RecipeRuleHelper;
import carpetamsaddition.logging.AmsCarpetLoggerRegistry;
import carpetamsaddition.network.payloads.handshake.HandShakeS2CPayload;
import carpetamsaddition.network.payloads.rule.commandCustomBlockHardness.CustomBlockHardnessPayload_S2C;
import carpetamsaddition.network.payloads.rule.commandSetPlayerPose.UpdatePlayerPosePayload_S2C;
import carpetamsaddition.translations.AMSTranslations;
import carpetamsaddition.translations.TranslationConstants;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.CountRulesUtil;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.NetworkUtil;

import com.google.common.collect.Maps;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CarpetAMSAdditionServer implements CarpetExtension {
    public static long serverStartTimeMillis;
    public static final int ruleCount = CountRulesUtil.countRules();
    public static final String fancyName = "Carpet AMS Addition";
    public static final String MOD_ID = CarpetAMSAdditionMod.getModId();
    public static final String compactName = MOD_ID.replace("-", "");  // carpetamsaddition
    public static final Logger LOGGER = LogManager.getLogger(fancyName);
    private static MinecraftServer minecraftServer;
    private static final CarpetAMSAdditionServer INSTANCE = new CarpetAMSAdditionServer();

    public static CarpetAMSAdditionServer getInstance() {
        return INSTANCE;
    }

    public MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

    public static void init() {
        CarpetServer.manageExtension(INSTANCE);
        AMSTranslations.loadTranslations();
    }

    @Override
    public void onGameStarted() {
        // let's /carpet handle our few simple settings
        LOGGER.info("{} v{} loaded! (Total rules: {})", fancyName, CarpetAMSAdditionMod.getVersion(), ruleCount);
        LOGGER.info("Open Source: https://github.com/Minecraft-AMS/Carpet-AMS-Addition");
        LOGGER.info("Issues: https://github.com/Minecraft-AMS/Carpet-AMS-Addition/issues");
        LOGGER.info("Wiki: https://minecraft-ams.github.io/carpetamsaddition/");
        CarpetServer.settingsManager.parseSettingsClass(CarpetAMSAdditionSettings.class);
    }

    @Override
    public String version() {
        return CarpetAMSAdditionMod.getModId();
    }

    @Override
    public void onTick(MinecraftServer server) {
        LeaderCommandRegistry.tick();
    }

    @Override
    public void registerLoggers() {
        AmsCarpetLoggerRegistry.registerLoggers();
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, final CommandBuildContext commandBuildContext) {
        RegisterCommands.registerCommands(dispatcher, commandBuildContext);
    }

    public void registerCustomRecipes(Map<Identifier, Recipe<?>> map, HolderLookup.Provider wrapperLookup) {
        AmsRecipeManager amsRecipeManager = new AmsRecipeManager(AmsRecipeBuilder.getInstance());
        AmsRecipeManager.clearRecipeListMemory(AmsRecipeBuilder.getInstance());
        CarpetAMSAdditionCustomRecipes.getInstance().buildRecipes();
        amsRecipeManager.registerRecipes(map, wrapperLookup);
    }

    public void sendS2CPacketOnHandShake(ServerPlayer player) {
        NetworkUtil.sendS2CPacket(player, HandShakeS2CPayload.create(CarpetAMSAdditionMod.getVersion(), NetworkUtil.isSupportServer()), NetworkUtil.SendMode.NEED_SUPPORT);
        NetworkUtil.sendS2CPacket(player, CustomBlockHardnessPayload_S2C.create(CustomBlockHardnessCommandRegistry.CUSTOM_BLOCK_HARDNESS_MAP), NetworkUtil.SendMode.NEED_SUPPORT);
        NetworkUtil.sendS2CPacket(player, UpdatePlayerPosePayload_S2C.create(SetPlayerPoseCommandRegistry.DO_POSE_MAP, player.getUUID()), NetworkUtil.SendMode.NEED_SUPPORT);
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayer player) {
        CustomWelcomeMessageConfig.getConfig().sendWelcomeMessage(player, MinecraftServerUtil.getServer());
        LeaderCommandRegistry.onPlayerLoggedIn(player);
        RecipeRuleHelper.onPlayerLoggedIn(MinecraftServerUtil.getServer(), player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayer player) {
        NetworkUtil.removeSupportClient(player.getUUID());
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        minecraftServer = server;
        serverStartTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        NetworkUtil.clearClientSupport();
        FancyFakePlayerNameTeamController.removeBotTeam(server, CarpetAMSAdditionSettings.fancyFakePlayerName);
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        NetworkUtil.setServerSupport(true);
        FancyFakePlayerNameTeamController.removeBotTeam(server, CarpetAMSAdditionSettings.fancyFakePlayerName);
    }

    public void afterServerLoadWorlds(MinecraftServer server) {
        LoadConfigFromJson.load(server);
        CommandHelper.updateAllCommandPermissions(server);
        RecipeRuleHelper.reloadServerResources(server);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        Map<String, String> trimmedTranslation = Maps.newHashMap();
        String prefix = TranslationConstants.CARPET_TRANSLATIONS_KEY_PREFIX;
        AMSTranslations.getTranslation(lang).forEach((key, value) -> {
            if (key.startsWith(prefix)) {
                String newKey = key.substring(prefix.length());
                newKey = "carpet." + newKey;
                trimmedTranslation.put(newKey, value);
            }
        });
        return trimmedTranslation;
    }
}
