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

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentHashMap;

public class PayloadHandlerChain {
    private final Map<Class<? extends AMS_CustomPayload>, CovariantHandlerList<?>> PAYLOAD_HANDLERS = new ConcurrentHashMap<>();

    public <T extends AMS_CustomPayload> void put(Class<T> payloadClass, Consumer<T> handler) {
        CovariantHandlerList<T> list = getOrCreate(payloadClass);
        list.addHandler(handler);
    }

    private <T extends AMS_CustomPayload> CovariantHandlerList<T> getOrCreate(Class<T> type) {
        CovariantHandlerList<?> existing = PAYLOAD_HANDLERS.get(type);

        if (existing != null) {
            return castList(existing, type);
        }

        CovariantHandlerList<T> newList = new CovariantHandlerList<>(type);
        PAYLOAD_HANDLERS.put(type, newList);

        return newList;
    }

    private <T extends AMS_CustomPayload> CovariantHandlerList<T> castList(CovariantHandlerList<?> list, Class<T> type) {
        if (list.matchesType(type)) {
            return list.asType(type);
        }

        throw new IllegalStateException("Type mismatch");
    }

    public boolean handle(AMS_CustomPayload payload) {
        if (payload == null) {
            return false;
        }

        CovariantHandlerList<?> list = PAYLOAD_HANDLERS.get(payload.getClass());

        return list != null && list.handle(payload);
    }

    private static class CovariantHandlerList<T extends AMS_CustomPayload> {
        private final Class<T> type;
        private final List<Consumer<T>> handlers = new ArrayList<>();

        CovariantHandlerList(Class<T> type) {
            this.type = type;
        }

        void addHandler(Consumer<T> handler) {
            handlers.add(handler);
        }

        boolean handle(AMS_CustomPayload payload) {
            if (!type.isInstance(payload)) {
                return false;
            }

            T typed = type.cast(payload);

            for (Consumer<T> handler : handlers) {
                handler.accept(typed);
            }

            return !handlers.isEmpty();
        }

        boolean matchesType(Class<? extends AMS_CustomPayload> otherType) {
            return type.equals(otherType);
        }

        <U extends AMS_CustomPayload> CovariantHandlerList<U> asType(Class<U> targetType) {
            if (!type.equals(targetType)) {
                throw new ClassCastException("Cannot cast " + type + " to " + targetType);
            }

            CovariantHandlerList<U> result = new CovariantHandlerList<>(targetType);

            for (Consumer<T> handler : handlers) {
                result.addHandler(createBridgeConsumer(handler));
            }

            return result;
        }

        private <U extends AMS_CustomPayload> Consumer<U> createBridgeConsumer(Consumer<T> originalHandler) {
            return u -> {
                if (type.isInstance(u)) {
                    T t = type.cast(u);
                    originalHandler.accept(t);
                }
            };
        }
    }
}
