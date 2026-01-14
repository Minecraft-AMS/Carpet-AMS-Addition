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

import carpetamsaddition.CarpetAMSAdditionLazySettings;
import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;

import net.minecraft.network.FriendlyByteBuf;

import java.util.EnumSet;

public class LazySettingsPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.LAZY_SETTINGS_S2C.getId();
    private final EnumSet<CarpetAMSAdditionLazySettings.Rule> rules;

    public LazySettingsPayload_S2C(EnumSet<CarpetAMSAdditionLazySettings.Rule> rules) {
        super(ID);
        this.rules = EnumSet.copyOf(rules);
    }

    public LazySettingsPayload_S2C(FriendlyByteBuf buf) {
        super(ID);

        int size = buf.readVarInt();
        this.rules = EnumSet.noneOf(CarpetAMSAdditionLazySettings.Rule.class);

        for (int i = 0; i < size; i++) {
            String ruleName = buf.readUtf();
            CarpetAMSAdditionLazySettings.Rule rule = CarpetAMSAdditionLazySettings.Rule.valueOf(ruleName);
            rules.add(rule);
        }
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeVarInt(rules.size());

        for (CarpetAMSAdditionLazySettings.Rule rule : rules) {
            buf.writeUtf(rule.name());
        }
    }

    @Override
    public void handle() {
        CarpetAMSAdditionLazySettings.clear();
        CarpetAMSAdditionLazySettings.addAll(this.rules);
    }

    public static LazySettingsPayload_S2C create(EnumSet<CarpetAMSAdditionLazySettings.Rule> rules) {
        return new LazySettingsPayload_S2C(rules);
    }
}
