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

import carpetamsaddition.commands.AmsCommandRegistry;
import carpetamsaddition.api.command.CommandRegistrationContext;
import carpetamsaddition.helpers.rule.commandPlayerChunkLoadController.PlayerChunkLoading;
import carpetamsaddition.settings.CarpetRuleRegistrar;
import carpetamsaddition.settings.AmsRuleMetadata;
import carpetamsaddition.api.recipe.AmsRecipeManager;
import carpetamsaddition.commands.rule.commandCustomBlockHardness.CustomBlockHardnessCommand;
import carpetamsaddition.commands.rule.commandPlayerLeader.LeaderCommand;
import carpetamsaddition.commands.rule.commandSetPlayerPose.SetPlayerPoseCommand;
import carpetamsaddition.config.LoadConfigFromJson;
import carpetamsaddition.config.rule.welcomeMessage.CustomWelcomeMessageConfig;
//#if MC>12006
import carpetamsaddition.helpers.FeatureChecker;
//#endif
import carpetamsaddition.helpers.rule.fancyFakePlayerName.FancyFakePlayerNameTeamController;
import carpetamsaddition.logging.AmsCarpetLoggerRegistry;
import carpetamsaddition.network.payloads.core.LazySettingsPayload_S2C;
import carpetamsaddition.network.payloads.handshake.HandShakeS2CPayload;
import carpetamsaddition.network.payloads.rule.commandCustomBlockHardness.CustomBlockHardnessPayload_S2C;
import carpetamsaddition.network.payloads.rule.commandSetPlayerPose.UpdatePlayerPosePayload_S2C;
import carpetamsaddition.translations.AMSTranslations;
import carpetamsaddition.translations.TranslationConstants;
import carpetamsaddition.api.command.CommandHelper;
import carpetamsaddition.utils.CountRulesUtil;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.NetworkUtil;

import carpetamsaddition.utils.PlayerUtil;
import com.google.common.collect.Maps;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
//#if MC>=11904
import net.minecraft.commands.CommandBuildContext;
//#endif
import net.minecraft.server.level.ServerPlayer;

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
        LOGGER.info("Wiki: https://carpet.mcams.club");
        CarpetRuleRegistrar.register(CarpetServer.settingsManager, CarpetAMSAdditionSettings.class);
    }

    @Override
    public String version() {
        return CarpetAMSAdditionMod.getModId();
    }

    @Override
    public void onTick(MinecraftServer server) {
        LeaderCommand.tick();
    }

    @Override
    public void registerLoggers() {
        AmsCarpetLoggerRegistry.registerLoggers();
    }

    @Override
    public void registerCommands(
        CommandDispatcher<CommandSourceStack> dispatcher
        //#if MC>=11904
        , final CommandBuildContext commandBuildContext
        //#endif
    ) {
        AmsCommandRegistry.registerAll(
            new CommandRegistrationContext(
            dispatcher
            //#if MC>=11904
            , commandBuildContext
            //#endif
        ));
    }

    public void sendS2CPacketOnHandShake(ServerPlayer player) {
        NetworkUtil.sendS2CPacket(player, HandShakeS2CPayload.create(CarpetAMSAdditionMod.getVersion(), NetworkUtil.getServerSupportState()), NetworkUtil.SendMode.NEED_SUPPORT);
        NetworkUtil.sendS2CPacket(player, CustomBlockHardnessPayload_S2C.create(CustomBlockHardnessCommand.CUSTOM_BLOCK_HARDNESS_MAP), NetworkUtil.SendMode.NEED_SUPPORT);
        NetworkUtil.sendS2CPacket(player, UpdatePlayerPosePayload_S2C.create(SetPlayerPoseCommand.DO_POSE_MAP, player.getUUID()), NetworkUtil.SendMode.NEED_SUPPORT);
        NetworkUtil.sendS2CPacket(player, LazySettingsPayload_S2C.create(AmsRuleMetadata.activeLazyRuleValues()), NetworkUtil.SendMode.NEED_SUPPORT);
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayer player) {
        CustomWelcomeMessageConfig.getConfig().sendWelcomeMessage(player, MinecraftServerUtil.getServer());
        LeaderCommand.onPlayerLoggedIn(player);
        AmsRecipeManager.onPlayerLoggedIn(MinecraftServerUtil.getServer(), player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayer player) {
        NetworkUtil.removeSupportClient(player.getUUID());
        PlayerChunkLoading.resetStatus(PlayerUtil.getName(player));
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        minecraftServer = server;
        serverStartTimeMillis = System.currentTimeMillis();
        AmsRuleMetadata.activateLazyRules();
        //#if MC>12006
        if (FeatureChecker.hasMinecartImprovements(server)) {
            FeatureChecker.EX_MINECART_FEATURE.set(true);
        }
        //#endif
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        AmsRuleMetadata.deactivateLazyRules();
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
        AmsRecipeManager.reloadServerResources(server);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        Map<String, String> trimmedTranslation = Maps.newHashMap();
        String prefix = TranslationConstants.CARPET_TRANSLATIONS_KEY_PREFIX;

        AMSTranslations.getTranslation(lang).forEach((key, value) -> {
            if (key.startsWith(prefix)) {
                String newKey = key.substring(prefix.length());
                //#if MC>=11900
                newKey = "carpet." + newKey;
                //#endif
                trimmedTranslation.put(newKey, value);
            }
        });

        return trimmedTranslation;
    }
}
