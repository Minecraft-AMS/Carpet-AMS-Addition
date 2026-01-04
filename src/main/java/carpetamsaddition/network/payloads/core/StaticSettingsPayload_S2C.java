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

import carpetamsaddition.CarpetAMSAdditionStaticSettings;
import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.network.AMS_PayloadManager;

import net.minecraft.network.FriendlyByteBuf;

import java.util.EnumSet;
import java.util.Set;

public class StaticSettingsPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.STATIC_SETTINGS_S2C.getId();
    private final Set<CarpetAMSAdditionStaticSettings.Rule> rules;

    private StaticSettingsPayload_S2C(EnumSet<CarpetAMSAdditionStaticSettings.Rule> rules) {
        super(ID);
        this.rules = EnumSet.copyOf(rules);
    }

    protected StaticSettingsPayload_S2C(FriendlyByteBuf buf) {
        super(ID);

        int size = buf.readVarInt();
        this.rules = EnumSet.noneOf(CarpetAMSAdditionStaticSettings.Rule.class);

        for (int i = 0; i < size; i++) {
            String ruleName = buf.readUtf();
            CarpetAMSAdditionStaticSettings.Rule rule = CarpetAMSAdditionStaticSettings.Rule.valueOf(ruleName);
            rules.add(rule);
        }
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        buf.writeVarInt(rules.size());

        for (CarpetAMSAdditionStaticSettings.Rule rule : rules) {
            buf.writeUtf(rule.name());
        }
    }

    @Override
    public void handle() {
        CarpetAMSAdditionStaticSettings.RULES.clear();
        CarpetAMSAdditionStaticSettings.RULES.addAll(this.rules);
    }

    public static void register() {
        AMS_PayloadManager.register(ID, StaticSettingsPayload_S2C::new);
    }

    public static StaticSettingsPayload_S2C create(EnumSet<CarpetAMSAdditionStaticSettings.Rule> rules) {
        return new StaticSettingsPayload_S2C(rules);
    }
}
