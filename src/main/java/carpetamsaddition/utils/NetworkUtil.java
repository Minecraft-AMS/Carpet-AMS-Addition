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

package carpetamsaddition.utils;

import carpet.api.settings.Rule;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.settings.AmsRuleCategory;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkUtil {
    private static final Set<UUID> SUPPORT_CLIENT = ConcurrentHashMap.newKeySet();
    private static final AtomicBoolean SUPPORT_SERVER = new AtomicBoolean(false);
    public static final Set<String> AMS_NETWORK_RULE_NAMES = new LinkedHashSet<>();

    public enum SendMode {
        FORCE,
        NEED_SUPPORT
    }

    public static void broadcastDataPack(AMS_CustomPayload payload, SendMode sendMode) {
        MinecraftServerUtil.getOnlinePlayers().forEach(player -> sendS2CPacket(player, payload, sendMode));
    }

    public static void sendS2CPacket(ServerPlayer player, AMS_CustomPayload payload, SendMode sendMode) {
        boolean shouldSend = switch (sendMode) {
            case FORCE -> true;
            case NEED_SUPPORT -> isSupportClient(player.getUUID());
        };

        if (shouldSend) {
            payload.sendS2CPacket(player);
        }
    }

    public static void sendC2SPacket(LocalPlayer player, AMS_CustomPayload payload, SendMode sendMode) {
        boolean shouldSend = switch (sendMode) {
            case FORCE -> true;
            case NEED_SUPPORT -> getServerSupportState();
        };

        if (shouldSend) {
            payload.sendC2SPacket(player);
        }
    }

    public static boolean isSupportClient(UUID uuid) {
        return SUPPORT_CLIENT.contains(uuid);
    }

    public static void setServerSupport(boolean support) {
        SUPPORT_SERVER.set(support);
    }

    public static boolean getServerSupportState() {
        return SUPPORT_SERVER.get();
    }

    public static Iterator<UUID> supportClientSetIterator() {
        return SUPPORT_CLIENT.iterator();
    }

    public static void addSupportClient(UUID uuid) {
        SUPPORT_CLIENT.add(uuid);
    }

    public static void removeSupportClient(UUID uuid) {
        SUPPORT_CLIENT.remove(uuid);
    }

    public static void clearClientSupport() {
        SUPPORT_CLIENT.clear();
    }

    public static void executeOnClientThread(Runnable runnable) {
        Optional.of(MinecraftClientUtil.clientIsRunning()).filter(Boolean::booleanValue).ifPresent(_ -> MinecraftClientUtil.getCurrentClient().execute(runnable));
    }

    public static void executeOnServerThread(Runnable runnable) {
        Optional.of(MinecraftServerUtil.serverIsRunning()).filter(Boolean::booleanValue).ifPresent(_ -> MinecraftServerUtil.getServer().execute(runnable));
    }

    public static void collectAmsNetworkRuleNames() {
        for (Field field : CarpetAMSAdditionSettings.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Rule.class)) {
                Rule ruleAnnotation = field.getAnnotation(Rule.class);
                for (String category : ruleAnnotation.categories()) {
                    if (category.equals(AmsRuleCategory.AMS_NETWORK)) {
                        AMS_NETWORK_RULE_NAMES.add(field.getName());
                        break;
                    }
                }
            }
        }
    }
}
