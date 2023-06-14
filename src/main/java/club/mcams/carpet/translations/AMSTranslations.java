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

import club.mcams.carpet.util.Messenger;
import club.mcams.carpet.AmsServer;
import club.mcams.carpet.mixin.translations.StyleAccessor;
import club.mcams.carpet.mixin.translations.TranslatableTextAccessor;
import club.mcams.carpet.util.FileUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Reference: Carpet TIS Addition
 */
public class AMSTranslations {
    private static final String LANG_DIR = String.format("assets/%s/lang", TranslationConstants.TRANSLATION_NAMESPACE);

    public static final Map<String, Map<String, String>> translations = Maps.newLinkedHashMap();
    public static final Set<String> languages = Sets.newHashSet();

    @SuppressWarnings("unchecked")
    private static List<String> getAvailableTranslations() {
        try {
            String dataStr = FileUtil.readFile(LANG_DIR + "/meta/languages.yml");
            Map<String, Object> yamlMap = new Yaml().load(dataStr);
            return (List<String>) yamlMap.get("languages");
        } catch (Exception e) {
            AmsServer.LOGGER.warn("Failed to load translations");
            return Lists.newArrayList();
        }
    }

    public static void loadTranslations() {
        getAvailableTranslations().forEach(AMSTranslations::loadTranslation);
    }

    public static void loadTranslation(String language) {
        String path = String.format("%s/%s.yml", LANG_DIR, language);
        String data;
        try {
            data = FileUtil.readFile(path);
        } catch (IOException e) {
            AmsServer.LOGGER.warn("Failed to load translation: " + language);
            return;
        }
        Map<String, Object> yaml = new Yaml().load(data);
        Map<String, String> translation = Maps.newLinkedHashMap();
        build(translation, yaml, "");
        translations.put(language, translation);
        languages.add(language);
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
        return translate(text, ((ServerPlayerEntityWithClientLanguage) player).getClientLanguage$AMS().toLowerCase());
    }

    public static BaseText translate(BaseText text) {
        return translate(text, getServerLanguage());
    }

    public static BaseText translate(BaseText text, String lang) {
        return translate(text, lang, false);
    }

    public static BaseText translate(BaseText text, String lang, boolean suppressWarnings) {
        //#if MC>=11900
        //$$ if (text.getContent() instanceof TranslatableTextContent) {
        //#else
        if (text instanceof TranslatableText) {
            //#endif
            //#if MC>=11900
            //$$ TranslatableTextContent translatableText = (TranslatableTextContent) text.getContent();
            //#else
            TranslatableText translatableText = (TranslatableText) text;
            //#endif
            if (translatableText.getKey().startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX)) {
                String formattedString = translateKeyToFormattedString(lang, translatableText.getKey());
                if (formattedString == null) {
                    // not supported language
                    formattedString = translateKeyToFormattedString(TranslationConstants.DEFAULT_LANGUAGE, translatableText.getKey());
                }
                if (formattedString != null) {
                    BaseText origin = text;
                    //#if MC>=11900
                    //$$ TranslatableTextAccessor fixedTranslatableText = (TranslatableTextAccessor) (Messenger.tr(formattedString, translatableText.getArgs())).getContent();
                    //#else
                    TranslatableTextAccessor fixedTranslatableText = (TranslatableTextAccessor) (new TranslatableText(formattedString, translatableText.getArgs()));
                    //#endif
                    try {
                        List<StringVisitable> translations = Lists.newArrayList();
                        //#if MC>=11800
                        fixedTranslatableText.invokeForEachPart(formattedString, translations::add);
                        //#else
                        //$$ fixedTranslatableText.invokeSetTranslation(formattedString);
                        //#endif
                        text = Messenger.c(translations.stream().map(stringVisitable -> {
                            if (stringVisitable instanceof BaseText) {
                                return (BaseText) stringVisitable;
                            }
                            return Messenger.s(stringVisitable.getString());
                        }).toArray());
                    } catch (TranslationException e) {
                        text = Messenger.s(formattedString);
                    }
                    text.getSiblings().addAll(origin.getSiblings());
                    text.setStyle(origin.getStyle());
                } else if (!suppressWarnings) {
                    AmsServer.LOGGER.warn("Unknown translation key {}", translatableText.getKey());
                }
            }
        }

        // translate hover text
        HoverEvent hoverEvent = ((StyleAccessor) text.getStyle()).getHoverEventField();
        if (hoverEvent != null) {
            Object hoverText = hoverEvent.getValue(hoverEvent.getAction());
            if (hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT && hoverText instanceof BaseText) {
                text.setStyle(text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, translate((BaseText) hoverText, lang))));
            }
        }

        // translate sibling texts
        List<Text> siblings = text.getSiblings();
        siblings.replaceAll(text1 -> translate((BaseText) text1, lang));
        return text;
    }
}
