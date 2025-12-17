/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpetamsaddition.translations;

import carpet.CarpetSettings;

import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.utils.FileUtil;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import top.byteeeee.yaml.TinyYamlParser;
import top.byteeeee.yaml.exception.YamlParseException;

import java.io.IOException;
import java.util.*;

public class AMSTranslations {
    private static final String LANG_DIR = String.format("assets/%s/lang", TranslationConstants.TRANSLATION_NAMESPACE);
    public static final Map<String, Map<String, String>> translations = Maps.newLinkedHashMap();
    public static final Set<String> languages = Sets.newHashSet();

    public static void loadTranslations() {
        try {
            List<String> availableLanguages = getAvailableLanguages();
            availableLanguages.forEach(language -> {
                try {
                    Map<String, String> translation = loadTranslationForLanguage(language);
                    translations.put(language, translation);
                    languages.add(language);
                } catch (IOException | YamlParseException e) {
                    CarpetAMSAdditionServer.LOGGER.warn("Failed to load translation for language: {}", language, e);
                }
            });
        } catch (IOException | YamlParseException e) {
            CarpetAMSAdditionServer.LOGGER.warn("Failed to get available languages", e);
        }
    }

    private static List<String> getAvailableLanguages() throws IOException, YamlParseException {
        String yamlData = FileUtil.readFile(LANG_DIR + "/meta/languages.yml");
        Map<String, Object> yamlMap = TinyYamlParser.parse(yamlData);
        Object languagesObj = yamlMap.getOrDefault("languages", new ArrayList<>());

        if (languagesObj instanceof List) {
            return TinyYamlParser.getNestedStringList(yamlMap, "languages");
        }

        return new ArrayList<>();
    }

    private static Map<String, String> loadTranslationForLanguage(String language) throws IOException, YamlParseException {
        String path = LANG_DIR + "/" + language + ".yml";
        String data = FileUtil.readFile(path);
        Map<String, Object> yaml = TinyYamlParser.parse(data);
        Map<String, String> translation = new LinkedHashMap<>();
        buildTranslationMap(translation, yaml, "");
        return translation;
    }

    @SuppressWarnings({"unchecked", "IfCanBeSwitch"})
    private static void buildTranslationMap(Map<String, String> translation, Map<String, Object> yaml, String prefix) {
        yaml.forEach((key, value) -> {
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;
            if (value instanceof String) {
                translation.put(fullKey, (String) value);
            } else if (value instanceof Map) {
                buildTranslationMap(translation, (Map<String, Object>) value, fullKey);
            } else if (value == null) {
                translation.put(fullKey, "");
            } else {
                translation.put(fullKey, String.valueOf(value));
            }
        });
    }

    public static String getServerLanguage() {
        return CarpetSettings.language.equalsIgnoreCase("none") ? TranslationConstants.DEFAULT_LANGUAGE : CarpetSettings.language;
    }

    @NotNull
    public static Map<String, String> getTranslation(String lang) {
        return translations.getOrDefault(lang, Collections.emptyMap());
    }

    @Nullable
    public static String translateKeyToFormattedString(String lang, String key) {
        return getTranslation(lang).get(key);
    }
}
