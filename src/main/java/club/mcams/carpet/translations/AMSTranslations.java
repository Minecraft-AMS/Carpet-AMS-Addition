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

package club.mcams.carpet.translations;

import carpet.CarpetSettings;

import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.AmsServer;
import club.mcams.carpet.mixin.translations.StyleAccessor;
import club.mcams.carpet.mixin.translations.TranslatableTextAccessor;
import club.mcams.carpet.utils.FileUtil;

import com.esotericsoftware.yamlbeans.YamlReader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

public class AMSTranslations {
    private static final String LANG_DIR = String.format("assets/%s/lang", TranslationConstants.TRANSLATION_NAMESPACE);
    public static final Map<String, Map<String, String>> translations = Maps.newLinkedHashMap();
    public static final Set<String> languages = Sets.newHashSet();

    @SuppressWarnings("unchecked")
    private static List<String> getAvailableTranslations() {
        String yamlData;
        try {                           
            yamlData = FileUtil.readFile(LANG_DIR + "/meta/languages.yml");
        } catch (Exception e) {
            AmsServer.LOGGER.warn("Failed to read file", e);
            return new ArrayList<>();
        }

        try (Reader reader = new StringReader(yamlData)) {
            Map<String, Object> yamlMap = (Map<String, Object>) new YamlReader(reader).read();
            List<String> languages = (List<String>) yamlMap.get("languages");
            return languages != null ? languages : new ArrayList<>();
        } catch (Exception e) {
            AmsServer.LOGGER.warn("Failed to load translations", e);
            return new ArrayList<>();
        }
    }

    public static void loadTranslations() {
        getAvailableTranslations().forEach(AMSTranslations::loadTranslation);
    }

    @SuppressWarnings("unchecked")
    public static void loadTranslation(String language) {
        String path = String.format("%s/%s.yml", LANG_DIR, language);
        String data;
        try {
            data = FileUtil.readFile(path);
        } catch (IOException e) {
            AmsServer.LOGGER.warn("Failed to load translation: " + language);
            return;
        }
        try (Reader reader = new StringReader(data)) {
            Map<String, Object> yaml = (Map<String, Object>) new YamlReader(reader).read();
            Map<String, String> translation = Maps.newLinkedHashMap();
            build(translation, yaml, "");
            translations.put(language, translation);
            languages.add(language);
        } catch (IOException e) {
            AmsServer.LOGGER.warn("Failed to load translation: " + language, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void build(Map<String, String> translation, Map<String, Object> yaml, String prefix) {
        yaml.forEach((key, value) -> {
            String fullKey = prefix.isEmpty() ? key : (!key.equals(".") ? prefix + "." + key : prefix);
            if (value instanceof String) {
                translation.put(fullKey, (String) value);
            } else if (value instanceof Map) {
                build(translation, (Map<String, Object>) value, fullKey);
            } else {
                throw new RuntimeException(String.format("Unknown type %s in with key %s", value.getClass(), fullKey));
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
        return getTranslation(lang.toLowerCase()).get(key);
    }

    public static BaseText translate(BaseText text, ServerPlayerEntity player) {
        if (player instanceof ServerPlayerEntityWithClientLanguage) {
            String lang = ((ServerPlayerEntityWithClientLanguage) player).getClientLanguage$AMS().toLowerCase();
            return translate(text, lang);
        }
        return text;
    }

    public static BaseText translate(BaseText text, String lang) {
        return translate(text, lang, false);
    }

    public static BaseText translate(BaseText text, String lang, boolean suppressWarnings) {
        if (!isTranslatable(text)) {
            return text;
        }
        TranslatableText translatableText = getTranslatableText(text);
        String translationKey = translatableText.getKey();
        if (!isTranslationKeyValid(translationKey)) {
            return text;
        }
        String formattedString = getFormattedTranslation(lang, translationKey);
        if (formattedString != null) {
            text = updateTextWithTranslation(text, formattedString, translatableText);
        } else if (!suppressWarnings) {
            AmsServer.LOGGER.warn("Unknown translation key {}", translationKey);
        }
        translateHoverText(text, lang);
        translateSiblingTexts(text, lang);
        return text;
    }

    private static boolean isTranslatable(BaseText text) {
        //#if MC>=11900
        //$$ return text.getContent() instanceof TranslatableTextContent;
        //#else
        return text instanceof TranslatableText;
        //#endif
    }

    private static TranslatableText getTranslatableText(BaseText text) {
        //#if MC>=11900
        //$$ return (TranslatableTextContent) text.getContent();
        //#else
        return (TranslatableText) text;
        //#endif
    }

    private static boolean isTranslationKeyValid(String key) {
        return key.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX);
    }

    private static String getFormattedTranslation(String lang, String key) {
        String formattedString = translateKeyToFormattedString(lang, key);
        if (formattedString == null) {
            formattedString = translateKeyToFormattedString(TranslationConstants.DEFAULT_LANGUAGE, key);
        }
        return formattedString;
    }

    private static BaseText updateTextWithTranslation(BaseText originalText, String formattedString, TranslatableText translatableText) {
        TranslatableTextAccessor fixedTranslatableText = (TranslatableTextAccessor) (Messenger.tr(formattedString, translatableText.getArgs()));
        BaseText newText = createNewText(formattedString, fixedTranslatableText);
        if (newText == null) {
            return Messenger.s(formattedString);
        }
        newText.getSiblings().addAll(originalText.getSiblings());
        newText.setStyle(originalText.getStyle());
        return newText;
    }

    private static BaseText createNewText(String formattedString, TranslatableTextAccessor fixedTranslatableText) {
        try {
            List<StringVisitable> translations = Lists.newArrayList();
            //#if MC>=11800
            fixedTranslatableText.invokeForEachPart(formattedString, translations::add);
            //#else
            //$$ fixedTranslatableText.invokeSetTranslation(formattedString);
            //#endif
            return Messenger.c((Object) translations.stream().map(stringVisitable -> {
                if (stringVisitable instanceof BaseText) {
                    return (BaseText) stringVisitable;
                }
                return Messenger.s(stringVisitable.getString());
            }).toArray(BaseText[]::new));
        } catch (TranslationException e) {
            return null;
        }
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    private static void translateHoverText(BaseText text, String lang) {
        HoverEvent hoverEvent = ((StyleAccessor) text.getStyle()).getHoverEventField();
        if (hoverEvent == null || hoverEvent.getAction() != HoverEvent.Action.SHOW_TEXT) {
            return;
        }
        Object hoverText = hoverEvent.getValue(hoverEvent.getAction());
        if (!(hoverText instanceof BaseText)) {
            return;
        }
        BaseText hoverBaseText = (BaseText) hoverText;
        hoverBaseText.setStyle(
            hoverBaseText.getStyle().withHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, translate(hoverBaseText, lang, false))
            )
        );
    }

    private static void translateSiblingTexts(BaseText text, String lang) {
        text.getSiblings().replaceAll(sibling -> translate((BaseText) sibling, lang, false));
    }
}