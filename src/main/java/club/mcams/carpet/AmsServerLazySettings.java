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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.function.BooleanSupplier;

public class AmsServerLazySettings {
    public static EnumSet<Rule> RULES = EnumSet.noneOf(Rule.class);

    public enum Rule {
        //#if MC>=12102
        //$$ EXPERIMENTAL_MINECART_ENABLED(() -> AmsServerSettings.experimentalMinecartEnabled),
        //#endif
        LARGE_SHULKER_BOX(() -> AmsServerSettings.largeShulkerBox);

        private final BooleanSupplier enabledSupplier;

        Rule(BooleanSupplier enabledSupplier) {
            this.enabledSupplier = enabledSupplier;
        }

        public boolean isEnabled() {
            return enabledSupplier.getAsBoolean();
        }
    }

    public static void initRules() {
        RULES.clear();
        RULES.addAll(Arrays.stream(Rule.values()).filter(Rule::isEnabled).collect(Collectors.toCollection(() -> EnumSet.noneOf(Rule.class))));
    }

    public static void clear() {
        if (RULES != null) {
            RULES.clear();
        }
    }

    public static void addAll(EnumSet<Rule> enumSet) {
        if (RULES != null && enumSet != null) {
            RULES.addAll(enumSet);
        }
    }

    public static boolean isEnabled(Rule rule) {
        return RULES != null && rule != null && RULES.contains(rule);
    }
}
