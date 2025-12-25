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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.MinecraftServerUtil;
import carpetamsaddition.utils.compat.DimensionWrapper;

import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class UpdateSuppressionContext {
    private static final Translator tr = new Translator("rule.amsUpdateSuppressionCrashFix");

    public static void sendMessageToServer(BlockPos pos, Level world, Throwable cause) {
        if (!Objects.equals(CarpetAMSAdditionSettings.amsUpdateSuppressionCrashFix, "silence")) {
            final Component copyButton = copyButton(pos);
            Messenger.sendServerMessage(
                MinecraftServerUtil.getServer(),
                Messenger.f(suppressionMessageText(pos, world, cause), Layout.RED, Layout.ITALIC).append(copyButton)
            );
        }
    }

    public static MutableComponent suppressionMessageText(BlockPos pos, Level world, Throwable cause) {
        DimensionWrapper dimension = getSuppressionDimension(world);
        String location = getSuppressionPos(pos);
        // Update suppression location @ minecraft:overworld -> [ 1, 0, -24 ] | reason: StackOverflowError
        return tr.tr("msg", dimension, location, exceptionCauseText(cause));
    }

    private static Component copyButton(BlockPos pos) {

        return Messenger.f(Messenger.s(" [C] ").setStyle(
            Messenger.simpleCopyButtonStyle(
            getSuppressionPos(pos).replace(",", ""), // 1, 0, -24 -> 1 0 -24
            tr.tr("copy"), Layout.YELLOW)
        ), Layout.GREEN, Layout.BOLD);
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
