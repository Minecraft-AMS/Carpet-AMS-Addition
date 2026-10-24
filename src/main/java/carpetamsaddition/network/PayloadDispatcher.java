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

package carpetamsaddition.network;

import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.utils.MinecraftClientUtil;

import carpetamsaddition.utils.MinecraftServerUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.concurrent.ConcurrentHashMap;

public class PayloadDispatcher {
    private final Map<Class<? extends AMS_CustomPayload>, Consumer<AMS_CustomPayload>> S2C_HANDLERS = new ConcurrentHashMap<>();
    private final Map<Class<? extends AMS_CustomPayload>, BiConsumer<AMS_CustomPayload, ServerPlayer>> C2S_HANDLERS = new ConcurrentHashMap<>();

    // S2C
    public <T extends AMS_CustomPayload> void register(Class<T> payloadClass, Consumer<T> handler) {
        Consumer<AMS_CustomPayload> adaptedHandler = payload -> handler.accept(payloadClass.cast(payload));
        S2C_HANDLERS.merge(payloadClass, adaptedHandler, Consumer::andThen);
    }

    // C2S
    public <T extends AMS_CustomPayload> void register(Class<T> payloadClass, BiConsumer<T, ServerPlayer> handler) {
        BiConsumer<AMS_CustomPayload, ServerPlayer> adaptedHandler = (payload, sender) -> handler.accept(payloadClass.cast(payload), sender);
        C2S_HANDLERS.merge(payloadClass, adaptedHandler, BiConsumer::andThen);
    }

    // S2C
    public boolean dispatch(AMS_CustomPayload payload) {
        if (payload == null) {
            return false;
        }

        Consumer<AMS_CustomPayload> handler = S2C_HANDLERS.get(payload.getClass());

        if (handler == null) {
            return false;
        }

        if (MinecraftClientUtil.clientIsRunning()) {
            MinecraftClientUtil.getCurrentClient().execute(() -> handler.accept(payload));
        }

        return true;
    }

    // C2S
    public boolean dispatch(AMS_CustomPayload payload, ServerPlayer sender) {
        if (payload == null) {
            return false;
        }

        BiConsumer<AMS_CustomPayload, ServerPlayer> handler = C2S_HANDLERS.get(payload.getClass());

        if (handler == null) {
            return false;
        }

        MinecraftServer server = EntityUtil.getEntityServer(sender);

        if (MinecraftServerUtil.serverIsRunning(server)) {
            EntityUtil.getEntityServer(sender).execute(() -> handler.accept(payload, sender));
        }

        return true;
    }
}
