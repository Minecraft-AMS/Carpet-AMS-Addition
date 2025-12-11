/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024 A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition. If not, see <https://www.gnu.org/licenses/>.
 */

package club.mcams.carpet.commands.rule.commandGetSystemInfo;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class GetSystemInfoCommandRegistry {
    private static final Translator translator = new Translator("command.getSystemInfo");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("getSystemInfo")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetSystemInfo))
            .executes(context -> executeGetSystemInfo(context.getSource().getPlayerOrException()))
        );
    }

    private static int executeGetSystemInfo(Player player) {
        String formatInfo = formatInfo();
        player.displayClientMessage(Messenger.s(formatInfo).withStyle(ChatFormatting.DARK_AQUA), false);
        return 1;
    }

    private static String formatInfo() {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        final String line = "===========================";
        String os = osBean.getName() + " - " + osBean.getVersion();
        String osArch = osBean.getArch();
        String availableProcessors = String.valueOf(runtime.availableProcessors());
        String maxMemory = runtime.maxMemory() / 1024 / 1024 + "MB";
        String totalMemory = runtime.totalMemory() / 1024 / 1024 + "MB";
        String freeMemory = runtime.freeMemory() / 1024 / 1024 + "MB";
        return String.format(
            "%s\n%s %s\n%s %s\n%s %s\n%s %s\n%s %s\n%s %s\n%s",
            line,
            translator.tr("os").getString(), os,
            translator.tr("os_arch").getString(), osArch,
            translator.tr("available_processors").getString(), availableProcessors,
            translator.tr("max_memory").getString(), maxMemory,
            translator.tr("total_memory").getString(), totalMemory,
            translator.tr("free_memory").getString(), freeMemory,
            line
        );
    }
}
