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
import carpet.api.settings.SettingsManager;

import carpetamsaddition.CarpetAMSAdditionServer;

//#if MC<11904
//$$ import carpetamsaddition.mixin.setting.ParsedRuleAccessor;
//$$ import carpetamsaddition.mixin.setting.SettingsManagerAccessor;
//$$ import carpetamsaddition.translations.AMSTranslations;
//$$ import carpetamsaddition.translations.TranslationConstants;
//$$ import org.jetbrains.annotations.Nullable;
//$$ import java.lang.annotation.Annotation;
//#endif

import com.google.common.collect.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class CarpetRuleRegistrar {
    private final SettingsManager settingsManager;
    private final List<CarpetRule<?>> rules = Lists.newArrayList();

    private CarpetRuleRegistrar(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public static void register(SettingsManager settingsManager, Class<?> settingsClass) {
        CarpetRuleRegistrar registrar = new CarpetRuleRegistrar(settingsManager);
        registrar.parseSettingsClass(settingsClass);
        registrar.registerToCarpet();
    }

    private void parseSettingsClass(Class<?> settingsClass) {
        for (Field field : settingsClass.getDeclaredFields()) {
            Rule rule = field.getAnnotation(Rule.class);
            if (rule != null) {
                this.parseRule(field, rule);
            }
        }
    }

    //#if MC<11904
    //$$ @SuppressWarnings("rawtypes")
    //$$ private void parseRule(Field field, Rule rule) {
    //$$     carpet.settings.Rule cmRule = new carpet.settings.Rule() {
    //$$         private final String basedKey = TranslationConstants.CARPET_TRANSLATIONS_KEY_PREFIX + "rule." + this.name() + ".";
    //$$
    //$$         @Nullable
    //$$         private String tr(String key) {
    //$$             return AMSTranslations.translateKeyToFormattedString(TranslationConstants.DEFAULT_LANGUAGE, this.basedKey + key);
    //$$         }
    //$$
    //$$         @Override
    //$$         public String desc() {
    //$$             String desc = this.tr("desc");
    //$$             if (desc == null) {
    //$$                 throw new NullPointerException(String.format("Rule %s has no translated desc", this.name()));
    //$$             }
    //$$             return desc;
    //$$         }
    //$$
    //$$         @Override
    //$$         public String[] extra() {
    //$$             List<String> extraMessages = Lists.newArrayList();
    //$$             for (int i = 0; ; i++) {
    //$$                 String message = this.tr("extra." + i);
    //$$                 if (message == null) {
    //$$                     break;
    //$$                 }
    //$$                 extraMessages.add(message);
    //$$             }
    //$$             return extraMessages.toArray(new String[0]);
    //$$         }
    //$$
    //$$         @Override
    //$$         public String name() {
    //$$             return field.getName();
    //$$         }
    //$$
    //$$         @Override
    //$$         public String[] category() {
    //$$             return rule.categories();
    //$$         }
    //$$
    //$$         @Override
    //$$         public String[] options() {
    //$$             return rule.options();
    //$$         }
    //$$
    //$$         @Override
    //$$         public boolean strict() {
    //$$             return rule.strict();
    //$$         }
    //$$
    //$$         @Override
    //$$         public Class<? extends carpet.settings.Validator>[] validate() {
    //$$             return rule.validators();
    //$$         }
    //$$
    //$$         @Override
    //$$         public Class<? extends Annotation> annotationType() {
    //$$             return rule.annotationType();
    //$$         }
    //$$
    //$$         @Override
    //$$         public String appSource() {
    //$$             return "";
    //$$         }
    //$$
    //$$         @SuppressWarnings("unchecked")
    //$$         @Override
    //$$         public Class<? extends carpet.settings.Condition>[] condition() {
    //$$             return new Class[0];
    //$$         }
    //$$     };
    //$$
    //$$     this.addRule(field, ParsedRuleAccessor.invokeConstructor(field, cmRule, this.settingsManager));
    //$$ }
    //#else
    private void parseRule(Field field, Rule rule) {
        try {
            Class<?> ruleAnnotationClass = Class.forName("carpet.settings.ParsedRule$RuleAnnotation");
            Constructor<?> annotationConstructor = ruleAnnotationClass.getDeclaredConstructors()[0];
            annotationConstructor.setAccessible(true);
            Object ruleAnnotation = annotationConstructor.newInstance(false, null, null, null, rule.categories(), rule.options(), rule.strict(), "", rule.validators());
            Class<?> parsedRuleClass = Class.forName("carpet.settings.ParsedRule");
            Constructor<?> parsedRuleConstructor = Arrays.stream(parsedRuleClass.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == 3)
                .filter(constructor -> constructor.getParameterTypes()[0] == Field.class)
                .filter(constructor -> constructor.getParameterTypes()[1].isAssignableFrom(ruleAnnotationClass))
                .filter(constructor -> constructor.getParameterTypes()[2] == SettingsManager.class)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Failed to get matched ParsedRule constructor"));
            parsedRuleConstructor.setAccessible(true);
            Object carpetRule = parsedRuleConstructor.newInstance(field, ruleAnnotation, this.settingsManager);
            if (carpetRule instanceof CarpetRule) {
                this.addRule(field, (CarpetRule<?>) carpetRule);
            } else {
                throw new ClassCastException("Failed to cast to CarpetRule.");
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException("Reflection error: " + e.getMessage(), e);
        } catch (IllegalAccessException | InstantiationException | IllegalArgumentException e) {
            throw new RuntimeException("Instantiation error: " + e.getMessage(), e);
        }
    }
    //#endif

    private void addRule(Field field, CarpetRule<?> rule) {
        this.rules.add(rule);
        AmsRuleMetadata.register(rule, field.isAnnotationPresent(PersistAsDefault.class), field.isAnnotationPresent(RecipeRule.class));
    }

    private void registerToCarpet() {
        //#if MC<11904
        //$$ for (ParsedRule<?> rule : this.rules) {
        //$$     Object existingRule = ((SettingsManagerAccessor) this.settingsManager).getRules$AMS().put(rule.name, rule);
        //$$     if (existingRule != null) {
        //$$         CarpetAMSAdditionServer.LOGGER.warn("Overwriting existing rule {}", existingRule);
        //$$     }
        //$$ }
        //#else
        for (CarpetRule<?> rule : this.rules) {
            try {
                this.settingsManager.addCarpetRule(rule);
            } catch (UnsupportedOperationException e) {
                CarpetAMSAdditionServer.LOGGER.warn("Failed to register rule {} to fabric carpet: {}", rule.name(), e);
            }
        }
        //#endif
    }
}
