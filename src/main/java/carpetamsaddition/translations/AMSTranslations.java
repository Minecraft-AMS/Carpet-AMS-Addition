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
import carpetamsaddition.utils.messageTextEvent.HoverEventUtil;
import carpetamsaddition.utils.messenger.Messenger;

import com.google.common.collect.Maps;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
//#if MC<11904
//$$ import net.minecraft.network.chat.BaseComponent;
//$$ import net.minecraft.network.chat.TranslatableComponent;
//#else
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
//#endif
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import top.byteeeee.yaml.TinyYamlParser;
import top.byteeeee.yaml.exception.YamlParseException;

import java.io.IOException;
import java.util.*;

public class AMSTranslations {
    private static final String LANG_DIR = String.format("assets/%s/lang", TranslationConstants.TRANSLATION_NAMESPACE);
    private static final Map<String, Map<String, String>> translations = Maps.newLinkedHashMap();

    public static void loadTranslations() {
        Map<String, Object> yaml = readTranslationYaml(LANG_DIR + "/meta/languages.yml");

        if (yaml == null || !(yaml.get("languages") instanceof List)) {
            return;
        }

        for (String language : TinyYamlParser.getNestedStringList(yaml, "languages")) {
            Map<String, Object> languageYaml = readTranslationYaml(LANG_DIR + "/" + language + ".yml");

            if (languageYaml == null) {
                continue;
            }

            Map<String, String> translation = new LinkedHashMap<>();
            buildTranslationMap(translation, languageYaml);

            translations.put(language, translation);
        }
    }

    @Nullable
    private static Map<String, Object> readTranslationYaml(String path) {
        try {
            return TinyYamlParser.parse(FileUtil.readFile(path));
        } catch (IOException | YamlParseException e) {
            CarpetAMSAdditionServer.LOGGER.warn("Failed to load translation YAML: {}", path, e);
            return null;
        }
    }

    private static void buildTranslationMap(Map<String, String> translation, Map<?, ?> yaml) {
        Deque<YamlFrame> stack = new ArrayDeque<>();
        stack.push(new YamlFrame(yaml, ""));

        while (!stack.isEmpty()) {

            YamlFrame frame = stack.peek();

            if (!frame.entries.hasNext()) {
                stack.pop();
                continue;
            }

            Map.Entry<?, ?> entry = frame.entries.next();
            String key = (String) entry.getKey();
            String fullKey = frame.prefix.isEmpty() ? key : frame.prefix + "." + key;
            Object value = entry.getValue();

            if (value instanceof Map) {
                stack.push(new YamlFrame((Map<?, ?>) value, fullKey));
            } else {
                translation.put(fullKey, Objects.toString(value, ""));
            }
        }
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
        String translated = getTranslation(lang).get(key);
        return translated != null ? translated : getTranslation(TranslationConstants.DEFAULT_LANGUAGE).get(key);
    }

    public static Component translate(Component text, ServerPlayer player) {
        if (!(player instanceof ServerPlayerEntityWithClientLanguage)) {
            return text;
        }

        String clientLang = ((ServerPlayerEntityWithClientLanguage) player).ams$getClientLanguage().toLowerCase();
        String serverLang = AMSTranslations.getServerLanguage();

        if (CarpetAMSAdditionSettings.amsTranslationMode == CarpetAMSAdditionSettings.translationModes.SERVER) {
            return translateTextComponent(text, serverLang);
        }

        return translateTextComponent(text, clientLang);
    }

    @NotNull
    private static Component translateTextComponent(@NotNull Component text, String lang) {
        return containsAMSTranslation(text) ? translateTextComponent(text, lang, Style.EMPTY) : text;
    }

    private static boolean containsAMSTranslation(Component text) {
        if (!isNativeComponent(text)) {
            return false;
        }

        String key = getTranslationKey(text);
        if (key != null) {
            if (key.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX)) {
                return true;
            }
            for (Object arg : getTranslationArgs(text)) {
                if (arg instanceof Component && containsAMSTranslation((Component) arg)) {
                    return true;
                }
            }
        }

        Component hoverText = HoverEventUtil.getText(text.getStyle().getHoverEvent());
        if (hoverText != null && containsAMSTranslation(hoverText)) {
            return true;
        }

        for (Component sibling : text.getSiblings()) {
            if (containsAMSTranslation(sibling)) {
                return true;
            }
        }

        return false;
    }

    @NotNull
    private static Component translateTextComponent(@NotNull Component text, String lang, Style parentStyle) {
        if (!isNativeComponent(text)) {
            return text;
        }

        Style style = translateStyle(text.getStyle(), lang).applyTo(parentStyle);
        String key = getTranslationKey(text);
        MutableComponent result;

        if (key == null) {
            result = Messenger.copy(text);
        } else {
            Object[] args = getTranslationArgs(text);
            Object[] translatedArgs = translateArguments(args, lang, style);
            String translated = key.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX) ? translateKeyToFormattedString(lang, key) : null;
            if (translated != null && !Objects.equals(translated, key)) {
                result = args.length == 0 ? Messenger.s(translated) : Messenger.tr(translated, translatedArgs);
            } else if (translatedArgs != args) {
                result = withTranslationArgs(text, key, translatedArgs);
            } else {
                result = Messenger.copy(text);
            }
        }

        Messenger.style(result, style);
        result.getSiblings().clear();

        for (Component sibling : text.getSiblings()) {
            result.append(translateTextComponent(sibling, lang, style));
        }

        return result;
    }

    private static Object[] translateArguments(Object[] args, String lang, Style style) {
        Object[] result = args;

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Component) {
                Component translated = translateTextComponent((Component) args[i], lang, style);
                if (translated != args[i]) {
                    if (result == args) {
                        result = Arrays.copyOf(args, args.length, Object[].class);
                    }
                    result[i] = translated;
                }
            }
        }

        return result;
    }

    private static Style translateStyle(Style style, String lang) {
        Component hoverText = HoverEventUtil.getText(style.getHoverEvent());

        if (hoverText == null) {
            return style;
        }

        Component translated = translateTextComponent(hoverText, lang);

        return translated == hoverText ? style : style.withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, translated));
    }

    private static boolean isNativeComponent(Component text) {
        //#if MC>=11904
        return text instanceof MutableComponent;
        //#else
        //$$ return text instanceof BaseComponent;
        //#endif
    }

    @Nullable
    private static String getTranslationKey(Component text) {
        //#if MC>=11904
        ComponentContents contents = text.getContents();
        return contents instanceof TranslatableContents ? ((TranslatableContents) contents).getKey() : null;
        //#else
        //$$ return text instanceof TranslatableComponent ? ((TranslatableComponent) text).getKey() : null;
        //#endif
    }

    private static Object[] getTranslationArgs(Component text) {
        //#if MC>=11904
        return ((TranslatableContents) text.getContents()).getArgs();
        //#else
        //$$ return ((TranslatableComponent) text).getArgs();
        //#endif
    }

    private static MutableComponent withTranslationArgs(Component text, String key, Object[] args) {
        //#if MC>=11904
        String fallback = ((TranslatableContents) text.getContents()).getFallback();
        return MutableComponent.create(new TranslatableContents(key, fallback, args));
        //#else
        //$$ return new TranslatableComponent(key, args);
        //#endif
    }
}
