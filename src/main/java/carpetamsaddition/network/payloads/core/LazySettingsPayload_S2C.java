/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026 A Minecraft Server and contributors
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

package carpetamsaddition.network.payloads.core;

import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.settings.AmsRuleMetadata;
import carpetamsaddition.utils.NetworkUtil;

import net.minecraft.network.FriendlyByteBuf;

import java.util.LinkedHashMap;
import java.util.Map;

public class LazySettingsPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.LAZY_SETTINGS_S2C.getId();
    private static final int MAX_RULE_COUNT = 256;
    private final Map<String, String> values;

    public LazySettingsPayload_S2C(Map<String, String> values) {
        super(ID);
        this.values = values == null ? new LinkedHashMap<>() : new LinkedHashMap<>(values);
    }

    public LazySettingsPayload_S2C(FriendlyByteBuf buf) {
        super(ID);

        int size = buf.readVarInt();
        if (size < 0 || size > MAX_RULE_COUNT) {
            throw new IllegalArgumentException("Invalid lazy rule count: " + size);
        }
        this.values = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            this.values.put(NetworkUtil.readBufString(buf), NetworkUtil.readBufString(buf));
        }
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeVarInt(values.size());
        for (Map.Entry<String, String> entry : values.entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeUtf(entry.getValue());
        }
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnClientThread(() -> AmsRuleMetadata.installClientLazyRuleValues(values));
    }

    public static LazySettingsPayload_S2C create(Map<String, String> values) {
        return new LazySettingsPayload_S2C(values);
    }
}
