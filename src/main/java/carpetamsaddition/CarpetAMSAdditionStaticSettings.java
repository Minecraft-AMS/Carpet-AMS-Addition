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

package carpetamsaddition;

import java.util.EnumSet;

public class CarpetAMSAdditionStaticSettings {
    public enum Rule {
        EXPERIMENTAL_MINECART_ENABLED,
        LARGE_SHULKER_BOX
    }

    public static final EnumSet<Rule> RULES = EnumSet.noneOf(Rule.class);

    /**
     * Call after Carpet loads the config
     * @see carpetamsaddition.mixin.hooks.settings.Carpet_SettingsManagerMixin
     */
    public static void addStaticSettings() {
        RULES.clear();

        conditionAdd(CarpetAMSAdditionSettings.largeShulkerBox, Rule.LARGE_SHULKER_BOX);
        conditionAdd(CarpetAMSAdditionSettings.experimentalMinecartEnabled, Rule.EXPERIMENTAL_MINECART_ENABLED);
    }

    private static void conditionAdd(boolean amsRule, Rule staticRule) {
        if (amsRule) {
            RULES.add(staticRule);
        }
    }

    public static boolean isEnabled(Rule rule) {
        return RULES.contains(rule);
    }
}
