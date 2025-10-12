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

package club.mcams.carpet.network.handler;

import club.mcams.carpet.network.payload.AMS_CustomPayload;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentHashMap;

public class PayloadHandlerChain {
    private final Map<Class<? extends AMS_CustomPayload>, List<Consumer<? extends AMS_CustomPayload>>> HANDLERS = new ConcurrentHashMap<>();

    public <T extends AMS_CustomPayload> void addHandlerFor(Class<T> payloadClass, Consumer<T> handler) {
        HANDLERS.computeIfAbsent(payloadClass, k -> new ArrayList<>()).add(handler);
    }

    public boolean handle(AMS_CustomPayload payload) {
        if (payload == null) {
            return false;
        }

        return handleHandlers(payload);
    }

    @SuppressWarnings("unchecked")
    private boolean handleHandlers(AMS_CustomPayload payload) {
        List<Consumer<? extends AMS_CustomPayload>> consumers = HANDLERS.get(payload.getClass());

        if (consumers != null && !consumers.isEmpty()) {
            for (Consumer<? extends AMS_CustomPayload> consumer : consumers) {
                ((Consumer<AMS_CustomPayload>) consumer).accept(payload);
            }
            return true;
        }

        return false;
    }
}
