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

package club.mcams.carpet.helpers.rule.amsUpdateSuppressionCrashFix;

import club.mcams.carpet.AmsServer;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.MessageTextEventUtils.ClickEventUtil;
import club.mcams.carpet.utils.MessageTextEventUtils.HoverEventUtil;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.compat.DimensionWrapper;
import club.mcams.carpet.utils.compat.LiteralTextUtil;

import net.minecraft.text.BaseText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThrowableSuppressionContext {
    private static final Translator translator = new Translator("rule.amsUpdateSuppressionCrashFix");

    private static String getSuppressionPos(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    private static DimensionWrapper getSuppressionDimension(World world) {
        return DimensionWrapper.of(world);
    }

    public static String suppressionMessageText(BlockPos pos, World world) {
        DimensionWrapper dimension = getSuppressionDimension(world);
        String location = getSuppressionPos(pos);
        // Update suppression location @ minecraft:overworld -> [ 1, 0, -24 ]
        return String.format("%s @ %s -> [ %s ]", translator.tr("location").getString(), dimension, location);
    }

    public static void sendMessageToServer(BlockPos pos, World world) {
        String message = suppressionMessageText(pos, world);
        BaseText hoverText = Messenger.s(translator.tr("copy").getString(), "y");
        String copyCoordText = getSuppressionPos(pos).replace(",", ""); // 1, 0, -24 -> 1 0 -24
        final Text COPY_BUTTON = LiteralTextUtil.compatText(" [C] ").setStyle(
            Style.EMPTY.withColor(Formatting.GREEN).withBold(true).
            withClickEvent(ClickEventUtil.event(ClickEventUtil.COPY_TO_CLIPBOARD, copyCoordText)).
            withHoverEvent(HoverEventUtil.event(HoverEventUtil.SHOW_TEXT, hoverText))
        );
        Messenger.sendServerMessage(
        AmsServer.minecraftServer,
        LiteralTextUtil.compatText(message).formatted(Formatting.RED, Formatting.ITALIC).append(COPY_BUTTON));
    }
}
