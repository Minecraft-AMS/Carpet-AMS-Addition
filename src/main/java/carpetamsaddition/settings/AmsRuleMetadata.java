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

package carpetamsaddition.settings;

import carpet.api.settings.CarpetRule;

import java.util.IdentityHashMap;
import java.util.Map;

public final class AmsRuleMetadata {
    private static final Map<CarpetRule<?>, RuleMetadata> RULE_METADATA = new IdentityHashMap<>();

    private AmsRuleMetadata() {}

    static void register(CarpetRule<?> rule, boolean mustSetDefault, boolean recipeRule) {
        RULE_METADATA.put(rule, new RuleMetadata(mustSetDefault, recipeRule));
    }

    public static boolean mustSetDefault(CarpetRule<?> rule) {
        RuleMetadata metadata = RULE_METADATA.get(rule);
        return metadata != null && metadata.mustSetDefault;
    }

    public static boolean hasActiveRecipeRule() {
        for (Map.Entry<CarpetRule<?>, RuleMetadata> entry : RULE_METADATA.entrySet()) {
            if (entry.getValue().recipeRule && Boolean.TRUE.equals(entry.getKey().value())) {
                return true;
            }
        }

        return false;
    }

    private static final class RuleMetadata {
        private final boolean mustSetDefault;
        private final boolean recipeRule;

        private RuleMetadata(boolean mustSetDefault, boolean recipeRule) {
            this.mustSetDefault = mustSetDefault;
            this.recipeRule = recipeRule;
        }
    }
}
