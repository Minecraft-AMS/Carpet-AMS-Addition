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

package carpetamsaddition.helpers.rule.amsUpdateSuppressionCrashFix;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.compat.DimensionWrapper;
import carpetamsaddition.utils.MessageTextEventUtils.ClickEventUtil;
import carpetamsaddition.utils.MessageTextEventUtils.HoverEventUtil;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class UpdateSuppressionContext {
    private static final Translator translator = new Translator("rule.amsUpdateSuppressionCrashFix");

    public static void sendMessageToServer(BlockPos pos, Level world, Throwable cause) {
        if (!Objects.equals(AmsServerSettings.amsUpdateSuppressionCrashFix, "silence")) {
            String suppressionMessage = suppressionMessageText(pos, world, cause);
            final Component copyButton = copyButton(pos);
            Messenger.sendServerMessage(
                MinecraftServerUtil.getServer(),
                Messenger.s(suppressionMessage).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC).append(copyButton)
            );
        }
    }

    public static String suppressionMessageText(BlockPos pos, Level world, Throwable cause) {
        DimensionWrapper dimension = getSuppressionDimension(world);
        String location = getSuppressionPos(pos);
        // Update suppression location @ minecraft:overworld -> [ 1, 0, -24 ] | reason: StackOverflowError
        return translator.tr("msg", dimension, location, exceptionCauseText(cause)).getString();
    }

    private static Component copyButton(BlockPos pos) {
        Component hoverText = Messenger.s(translator.tr("copy")).withStyle(ChatFormatting.YELLOW);
        String copyCoordText = getSuppressionPos(pos).replace(",", ""); // 1, 0, -24 -> 1 0 -24

        return
            Messenger.s(" [C] ").setStyle(
                Style.EMPTY.withColor(ChatFormatting.GREEN).withBold(true).
                withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, copyCoordText)).
                withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, hoverText))
            );
    }

    private static String getSuppressionPos(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    private static DimensionWrapper getSuppressionDimension(Level world) {
        return DimensionWrapper.of(world);
    }

    private static String exceptionCauseText(Throwable cause) {
        return cause.getClass().getSimpleName();
    }
}
