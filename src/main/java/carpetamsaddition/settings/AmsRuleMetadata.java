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
import carpet.api.settings.Validator;

import net.minecraft.commands.CommandSourceStack;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Collection;
import java.util.Collections;

public final class AmsRuleMetadata {
    private static final Map<CarpetRule<?>, RuleMetadata> RULE_METADATA = new IdentityHashMap<>();
    private static final Map<String, RuleMetadata> LAZY_RULES = new LinkedHashMap<>();
    private static final Map<Field, Object> CLIENT_ORIGINAL_LAZY_VALUES = new IdentityHashMap<>();
    private static boolean lazyRulesActive;

    private AmsRuleMetadata() {}

    static void register(CarpetRule<?> rule, Field field, Rule definition, boolean recipeRule) {
        RuleMetadata metadata = new RuleMetadata(field, definition, recipeRule);
        RULE_METADATA.put(rule, metadata);
        if (metadata.lazy) {
            LAZY_RULES.put(field.getName(), metadata);
        }
    }

    public static boolean mustSetDefault(CarpetRule<?> rule) {
        RuleMetadata metadata = RULE_METADATA.get(rule);
        return metadata != null && metadata.mustSetDefault;
    }

    public static void activateLazyRules() {
        lazyRulesActive = true;
    }

    public static void deactivateLazyRules() {
        lazyRulesActive = false;
    }

    public static boolean shouldDeferLazyRule(CarpetRule<?> rule) {
        RuleMetadata metadata = RULE_METADATA.get(rule);
        return lazyRulesActive && metadata != null && metadata.lazy;
    }

    public static String validateLazyRuleValue(CommandSourceStack source, CarpetRule<?> rule, String input) {
        RuleMetadata metadata = RULE_METADATA.get(rule);

        if (metadata == null || !metadata.lazy) {
            throw new IllegalArgumentException("Not a lazy rule");
        }

        //#if MC>=11904
        Collection<String> options = rule.suggestions();
        //#else
        //$$ Collection<String> options = rule.options;
        //#endif
        validateValueText(input, metadata.strict, options);
        Class<?> type = metadata.field.getType();
        Object value = parseValue(type, input);

        for (Validator<?> validator : metadata.validators) {
            value = validateValue(validator, source, rule, value, input);
            if (value == null) {
                return null;
            }
        }

        String result = formatValue(value);
        validateValueText(result, metadata.strict, options);
        parseValue(type, result);

        return result;
    }

    public static Map<String, String> activeLazyRuleValues() {
        Map<String, String> values = new LinkedHashMap<>();

        for (Map.Entry<String, RuleMetadata> entry : LAZY_RULES.entrySet()) {
            values.put(entry.getKey(), String.valueOf(getFieldValue(entry.getValue().field)));
        }

        return values;
    }

    public static void installClientLazyRuleValues(Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            RuleMetadata metadata = LAZY_RULES.get(entry.getKey());

            if (metadata == null) {
                continue;
            }

            Object value = parseValue(metadata.field.getType(), entry.getValue());
            CLIENT_ORIGINAL_LAZY_VALUES.putIfAbsent(metadata.field, getFieldValue(metadata.field));

            setFieldValue(metadata.field, value);
        }
    }

    public static void clearClientLazyRuleValues() {
        for (Map.Entry<Field, Object> entry : CLIENT_ORIGINAL_LAZY_VALUES.entrySet()) {
            setFieldValue(entry.getKey(), entry.getValue());
        }

        CLIENT_ORIGINAL_LAZY_VALUES.clear();
    }

    public static boolean hasActiveRecipeRule() {
        for (Map.Entry<CarpetRule<?>, RuleMetadata> entry : RULE_METADATA.entrySet()) {
            if (entry.getValue().recipeRule && Boolean.TRUE.equals(entry.getKey().value())) {
                return true;
            }
        }

        return false;
    }

    private static Object getFieldValue(Field field) {
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not read rule field " + field.getName(), e);
        }
    }

    private static void setFieldValue(Field field, Object value) {
        try {
            field.set(null, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not write rule field " + field.getName(), e);
        }
    }

    private static void validateValueText(String value, boolean strict, Collection<String> options) {
        if (value == null || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            throw new IllegalArgumentException("Invalid configuration value");
        }

        if (strict && !options.isEmpty() && !options.contains(value)) {
            throw new IllegalArgumentException("Value is not one of the rule options");
        }
    }

    private static String formatValue(Object value) {
        return value instanceof Enum ? ((Enum<?>) value).name().toLowerCase(Locale.ROOT) : String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    private static <T> T validateValue(Validator<T> validator, CommandSourceStack source, CarpetRule<?> rule, Object value, String input) {
        CarpetRule<T> typedRule = (CarpetRule<T>) rule;
        T result = validator.validate(source, typedRule, (T) value, input);

        if (result == null && source != null) {
            validator.notifyFailure(source, typedRule, input);
        }

        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object parseValue(Class<?> type, String value) {
        if (type == Boolean.TYPE || type == Boolean.class) {
            if (!"true".equals(value) && !"false".equals(value)) {
                throw new IllegalArgumentException("Expected true or false");
            }
            return Boolean.valueOf(value);
        }

        if (type == Integer.TYPE || type == Integer.class) {
            return Integer.valueOf(value);
        }

        if (type == Long.TYPE || type == Long.class) {
            return Long.valueOf(value);
        }

        if (type == Float.TYPE || type == Float.class) {
            float parsed = Float.parseFloat(value);

            if (!Float.isFinite(parsed)) {
                throw new IllegalArgumentException("Expected a finite number");
            }

            return parsed;
        }

        if (type == Double.TYPE || type == Double.class) {
            double parsed = Double.parseDouble(value);

            if (!Double.isFinite(parsed)) {
                throw new IllegalArgumentException("Expected a finite number");
            }

            return parsed;
        }

        if (type == String.class) {
            return value;
        }

        if (type.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) type, value.toUpperCase(Locale.ROOT));
        }

        throw new IllegalArgumentException("Unsupported lazy rule type " + type.getName());
    }

    private static List<Validator<?>> createValidators(Field field, Rule definition) {
        List<Validator<?>> validators = new ArrayList<>();

        for (Class<?> validatorClass : definition.validators()) {
            if (RuleObserver.class.isAssignableFrom(validatorClass)) {
                continue;
            }

            try {
                Constructor<?> constructor = validatorClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                validators.add((Validator<?>) constructor.newInstance());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Could not instantiate validator for " + field.getName(), e);
            }
        }

        return validators;
    }

    private static final class RuleMetadata {
        private final Field field;
        private final boolean mustSetDefault;
        private final boolean lazy;
        private final boolean recipeRule;
        private final boolean strict;
        private final List<Validator<?>> validators;

        private RuleMetadata(Field field, Rule definition, boolean recipeRule) {
            this.field = field;
            this.lazy = field.isAnnotationPresent(LazyRule.class);
            this.mustSetDefault = this.lazy || field.isAnnotationPresent(PersistAsDefault.class);
            this.recipeRule = recipeRule;
            this.strict = definition.strict();
            this.validators = this.lazy ? createValidators(field, definition) : Collections.emptyList();
        }
    }
}
