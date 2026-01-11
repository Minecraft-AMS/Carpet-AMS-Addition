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
import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.FileUtil;

import carpetamsaddition.utils.Messenger;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
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

    public static MutableComponent translate(MutableComponent text, ServerPlayer player) {
        if (!(player instanceof ServerPlayerEntityWithClientLanguage)) {
            return text;
        }

        String clientLang = ((ServerPlayerEntityWithClientLanguage) player).getClientLanguage$AMS();
        String serverLang = AMSTranslations.getServerLanguage();

        if (CarpetAMSAdditionSettings.amsTranslationMode == CarpetAMSAdditionSettings.translationModes.SERVER) {
            return translateTextComponent(text, serverLang);
        }

        return translateTextComponent(text, clientLang);
    }

    @NotNull
    private static MutableComponent translateTextComponent(@NotNull MutableComponent text, String lang) {
        if (text.getContents() instanceof TranslatableContents translatableContents) {

            String key = translatableContents.getKey();

            if (key.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX)) {

                String translated = translateKeyToFormattedString(lang, key);

                if (translated != null && !translated.equals(key)) {
                    Object[] args = translatableContents.getArgs();
                    if (args.length > 0) {

                        Object[] translatedArgs = new Object[args.length];

                        for (int i = 0; i < args.length; i++) {
                            if (args[i] instanceof MutableComponent argComponent) {
                                translatedArgs[i] = translateTextComponent(argComponent, lang);
                            } else {
                                translatedArgs[i] = args[i];
                            }
                        }

                        translated = String.format(translated, translatedArgs);
                    }

                    MutableComponent newComponent = Messenger.s(translated).setStyle(text.getStyle());

                    for (Component sibling : text.getSiblings()) {
                        if (sibling instanceof MutableComponent mutableSibling) {
                            newComponent.append(translateTextComponent(mutableSibling, lang));
                        } else {
                            newComponent.append(sibling);
                        }
                    }

                    return newComponent;
                }
            }
        }

        MutableComponent result = Messenger.copy(text);
        result.getSiblings().clear();

        for (Component sibling : text.getSiblings()) {
            if (sibling instanceof MutableComponent mutableSibling) {
                result.append(translateTextComponent(mutableSibling, lang));
            } else {
                result.append(sibling);
            }
        }

        return result;
    }
}
