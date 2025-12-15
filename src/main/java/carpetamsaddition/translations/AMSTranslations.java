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

import carpetamsaddition.utils.Messenger;
import carpetamsaddition.CarpetAMSAdditionServer;
import carpetamsaddition.utils.FileUtil;
import carpetamsaddition.mixin.translations.TranslatableTextAccessor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.contents.TranslatableFormatException;
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
        if (player instanceof ServerPlayerEntityWithClientLanguage) {
            String lang = ((ServerPlayerEntityWithClientLanguage) player).getClientLanguage$AMS().toLowerCase();
            return translate(text, lang);
        }
        return text;
    }

    public static MutableComponent translate(MutableComponent text, String lang) {
        return translate(text, lang, false);
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    public static MutableComponent translate(MutableComponent text, String lang, boolean suppressWarnings) {
        if (!(text.getContents() instanceof TranslatableContents)) {
            return text;
        }
        TranslatableContents translatableText = (TranslatableContents) text.getContents();
        String translationKey = translatableText.getKey();
        if (!translationKey.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX)) {
            return text;
        }
        String formattedString = Optional.ofNullable(translateKeyToFormattedString(lang, translationKey)).orElseGet(() -> translateKeyToFormattedString(TranslationConstants.DEFAULT_LANGUAGE, translationKey));
        return formattedString != null ? updateTextWithTranslation(text, formattedString, translatableText) : translationLog(translationKey, suppressWarnings, text);
    }

    private static MutableComponent translationLog(String translationKey, boolean suppressWarnings, MutableComponent text) {
        if (!suppressWarnings) {
            CarpetAMSAdditionServer.LOGGER.warn("Unknown translation key: {}. Check if the translation exists or the key is correct.", translationKey);
        }
        return text;
    }

    private static MutableComponent updateTextWithTranslation(MutableComponent originalText, String formattedString, TranslatableContents translatableText) {
        TranslatableTextAccessor fixedTranslatableText = (TranslatableTextAccessor) translatableText;
        MutableComponent newText = createNewText(formattedString, fixedTranslatableText);
        if (newText == null) {
            return Messenger.s(formattedString);
        } else {
            newText.getSiblings().addAll(originalText.getSiblings());
            newText.setStyle(originalText.getStyle());
            return newText;
        }
    }

    private static MutableComponent createNewText(String formattedString, TranslatableTextAccessor fixedTranslatableText) {
        try {
            List<FormattedText> translations = Lists.newArrayList();
            fixedTranslatableText.invokeDecomposeTemplate(formattedString, translations::add);
            MutableComponent[] textArray = new MutableComponent[translations.size()];
            for (int i = 0; i < translations.size(); i++) {
                FormattedText stringVisitable = translations.get(i);
                if (stringVisitable instanceof MutableComponent) {
                    textArray[i] = (MutableComponent) stringVisitable;
                } else {
                    textArray[i] = Messenger.s(stringVisitable.getString());
                }
            }
            return Messenger.c((Object) textArray);
        } catch (TranslatableFormatException e) {
            return null;
        }
    }
}
