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

package club.mcams.carpet.util;

import club.mcams.carpet.mixin.translations.StyleAccessor;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.util.compat.DimensionWrapper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import com.google.common.collect.ImmutableMap;

//#if MC>=11900
//$$ import java.util.function.Supplier;
//#endif

import org.jetbrains.annotations.Nullable;

/**
 * Reference: Carpet TIS Addition
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Messenger {
    private static final Translator translator = new Translator("util");

    // Compound Text
    public static BaseText c(Object... fields) {
        //#if MC>=11900
        //$$ return (MutableText) carpet.utils.Messenger.c(fields);
        //#else
        return carpet.utils.Messenger.c(fields);
        //#endif
    }

    // Simple Text
    public static BaseText s(Object text) {
        //#if MC>=11900
        //$$ return Text.literal(text.toString());
        //#else
        return new LiteralText(text.toString());
        //#endif
    }

    // Simple Text with carpet style
    public static BaseText s(Object text, String carpetStyle) {
        return formatting(s(text), carpetStyle);
    }

    // Simple Text with formatting
    public static BaseText s(Object text, Formatting textFormatting) {
        return formatting(s(text), textFormatting);
    }

    // Fancy Text
    public static BaseText fancy(String carpetStyle, BaseText displayText, BaseText hoverText, ClickEvent clickEvent) {
        BaseText text = copy(displayText);
        if (carpetStyle != null) {
            text.setStyle(parseCarpetStyle(carpetStyle));
        }
        if (hoverText != null) {
            hover(text, hoverText);
        }
        if (clickEvent != null) {
            click(text, clickEvent);
        }
        return text;
    }

    public static BaseText fancy(BaseText displayText, BaseText hoverText, ClickEvent clickEvent) {
        return fancy(null, displayText, hoverText, clickEvent);
    }

    // Translation Text
    public static BaseText tr(String key, Object... args) {
        //#if MC>=11900
        //$$ return Text.translatable(key, args);
        //#else
        return new TranslatableText(key, args);
        //#endif
    }

    public static BaseText copy(BaseText text) {
        return (BaseText) text.shallowCopy();
    }

    private static void __tell(ServerCommandSource source, BaseText text, boolean broadcastToOps)
    {
        source.sendFeedback(
                //#if MC >= 12000
                //$$ () ->
                //#endif
                text, broadcastToOps
        );
    }

    public static void tell(ServerCommandSource source, BaseText text, boolean broadcastToOps) {
        __tell(source, text, broadcastToOps);
    }
    public static void tell(PlayerEntity player, BaseText text, boolean broadcastToOps) {
        tell(player.getCommandSource(), text, broadcastToOps);
    }
    public static void tell(ServerCommandSource source, BaseText text) {
        tell(source, text, false);
    }

    public static void tell(PlayerEntity player, BaseText text)
    {
        tell(player, text, false);
    }

    public static void tell(ServerCommandSource source, Iterable<BaseText> texts, boolean broadcastToOps) {
        texts.forEach(text -> tell(source, text, broadcastToOps));
    }
    public static void tell(PlayerEntity player, Iterable<BaseText> texts, boolean broadcastToOps) {
        texts.forEach(text -> tell(player, text, broadcastToOps));
    }
    public static void tell(ServerCommandSource source, Iterable<BaseText> texts) {
        tell(source, texts, false);
    }
    public static void tell(PlayerEntity player, Iterable<BaseText> texts)
    {
        tell(player, texts, false);
    }

    public static BaseText formatting(BaseText text, Formatting... formattings) {
        text.formatted(formattings);
        return text;
    }

    public static BaseText formatting(BaseText text, String carpetStyle) {
        Style textStyle = text.getStyle();
        StyleAccessor parsedStyle = (StyleAccessor) parseCarpetStyle(carpetStyle);
        textStyle =  textStyle.withColor(parsedStyle.getColorField());
        textStyle = textStyle.withBold(parsedStyle.getBoldField());
        textStyle = textStyle.withItalic(parsedStyle.getItalicField());
        ((StyleAccessor) textStyle).setUnderlinedField(parsedStyle.getUnderlineField());
        ((StyleAccessor) textStyle).setStrikethroughField(parsedStyle.getStrikethroughField());
        ((StyleAccessor) textStyle).setObfuscatedField(parsedStyle.getObfuscatedField());
        return style(text, textStyle);
    }

    public static BaseText style(BaseText text, Style style) {
        text.setStyle(style);
        return text;
    }

    public static BaseText click(BaseText text, ClickEvent clickEvent) {
        style(text, text.getStyle().withClickEvent(clickEvent));
        return text;
    }

    public static BaseText hover(BaseText text, HoverEvent hoverEvent) {
        style(text, text.getStyle().withHoverEvent(hoverEvent));
        return text;
    }

    public static BaseText hover(BaseText text, BaseText hoverText) {
        return hover(text, new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
    }

    public static BaseText entity(String style, Entity entity) {
        BaseText entityBaseName = (BaseText) entity.getType().getName();
        BaseText entityDisplayName = (BaseText) entity.getName();
        BaseText hoverText = Messenger.c(
            translator.tr("entity_type", entityBaseName, s(EntityType.getId(entity.getType()).toString())), newLine(),
            getTeleportHint(entityDisplayName)
        );
        return fancy(style, entityDisplayName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(entity)));
    }

    private static BaseText getTeleportHint(BaseText dest) {
        return translator.tr("teleport_hint", dest);
    }

    public static BaseText newLine() {
        return s("\n");
    }

    private static final ImmutableMap<DimensionWrapper, BaseText> DIMENSION_NAME = ImmutableMap.of(
        DimensionWrapper.OVERWORLD, tr("createWorld.customize.preset.overworld"),
        DimensionWrapper.THE_NETHER, tr("advancements.nether.root.title"),
        DimensionWrapper.THE_END, tr("advancements.end.root.title")
    );

    public static BaseText dimension(DimensionWrapper dim) {
        BaseText dimText = DIMENSION_NAME.get(dim);
        return dimText != null ? copy(dimText) : Messenger.s(dim.getIdentifierString());
    }

    private static BaseText __coord(String style, @Nullable DimensionWrapper dim, String posStr, String command) {
        BaseText hoverText = Messenger.s("");
        hoverText.append(getTeleportHint(Messenger.s(posStr)));
        if (dim != null) {
            hoverText.append("\n");
            hoverText.append(translator.tr("teleport_hint.dimension"));
            hoverText.append(": ");
            hoverText.append(dimension(dim));
        }
        return fancy(style, Messenger.s(posStr), hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
    }

    public static BaseText coord(String style, Vec3d pos, DimensionWrapper dim) {
        return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));
    }

    public static BaseText coord(String style, Vec3i pos, DimensionWrapper dim) {
        return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));
    }

    public static BaseText coord(String style, ChunkPos pos, DimensionWrapper dim) {
        return __coord(style, dim, TextUtil.coord(pos), TextUtil.tp(pos, dim));
    }

    public static BaseText coord(String style, Vec3d pos) {
        return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));
    }

    public static BaseText coord(String style, Vec3i pos) {
        return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));
    }

    public static BaseText coord(String style, ChunkPos pos) {
        return __coord(style, null, TextUtil.coord(pos), TextUtil.tp(pos));
    }

    public static BaseText coord(Vec3d pos, DimensionWrapper dim) {
        return coord(null, pos, dim);
    }

    public static BaseText coord(Vec3i pos, DimensionWrapper dim) {
        return coord(null, pos, dim);
    }

    public static BaseText coord(ChunkPos pos, DimensionWrapper dim) {
        return coord(null, pos, dim);
    }

    public static BaseText coord(Vec3d pos) {
        return coord(null, pos);
    }

    public static BaseText coord(Vec3i pos) {
        return coord(null, pos);
    }

    public static BaseText coord(ChunkPos pos) {
        return coord(null, pos);
    }

    public static Style parseCarpetStyle(String style) {
        return carpet.utils.Messenger.parseStyle(style);
    }
}
