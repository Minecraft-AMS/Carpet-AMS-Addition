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

package club.mcams.carpet;

import java.util.EnumSet;

public class AmsServerStaticSettings {
    public enum Rule {
        //#if MC>=12102
        //$$ EXPERIMENTAL_MINECART_ENABLED,
        //#endif
        LARGE_SHULKER_BOX
    }

    public static final EnumSet<Rule> ENABLED_RULES = EnumSet.noneOf(Rule.class);

    public static void Assignment() {
        ENABLED_RULES.clear();

        conditionAdd(AmsServerSettings.largeShulkerBox, Rule.LARGE_SHULKER_BOX);
        //#if MC>=12102
        //$$ conditionAdd(AmsServerSettings.experimentalMinecartEnabled, Rule.EXPERIMENTAL_MINECART_ENABLED);
        //#endif
    }

    @SuppressWarnings("SameParameterValue")
    private static void conditionAdd(boolean amsRule, Rule staticRule) {
        if (amsRule) {
            ENABLED_RULES.add(staticRule);
        }
    }

    public static boolean isEnabled(Rule rule) {
        return ENABLED_RULES.contains(rule);
    }
}
