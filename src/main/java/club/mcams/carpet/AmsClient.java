/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package club.mcams.carpet;

import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.network.payloads.handshake.HandShakeC2SPayload;
import club.mcams.carpet.utils.MinecraftClientUtil;
import club.mcams.carpet.utils.NetworkUtil;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AmsClient implements ClientModInitializer {
    private static final String MOD_ID = "carpet-ams-addition";
    public static final String fancyName = "Carpet AMS Addition";
    public static final String name = getModId();
    public static MinecraftClient minecraftClient;
    public static final Logger LOGGER = LogManager.getLogger(fancyName);
    public ClientPlayerEntity player;
    private static String version;
    private static final AmsClient AMS_CLIENT_INSTANCE = new AmsClient();

    @Override
    public void onInitializeClient() {
        version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();
        minecraftClient = MinecraftClient.getInstance();
        AMS_PayloadManager.registerPayloads();
    }

    public static AmsClient getInstance() {
        return AMS_CLIENT_INSTANCE;
    }

    public static String getModId() {
        return MOD_ID;
    }

    public static String getVersion() {
        return version;
    }

    public void onGameJoin() {
        player = MinecraftClientUtil.getCurrentPlayer();
        NetworkUtil.sendC2SPacket(player, HandShakeC2SPayload.create(version, player.getUuid()), NetworkUtil.SendMode.FORCE);
    }

    public void onDisconnect() {
        NetworkUtil.setServerSupport(false);
        AmsServerLazySettings.clear();
    }

    public void onTick() {
        // On client tick
    }
}
