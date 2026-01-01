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
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class GetSystemInfoCommandRegistry {
    private static final Translator tr = new Translator("command.getSystemInfo");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("getSystemInfo")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetSystemInfo))
            .executes(context -> executeGetSystemInfo(context.getSource()))
        );
    }

    private static int executeGetSystemInfo(ServerCommandSource source) {
        Messenger.tell(source, Messenger.f(formatInfo(), Layout.DARK_AQUA));
        return 1;
    }

    private static BaseText formatInfo() {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        String os = osBean.getName() + " - " + osBean.getVersion();
        String osArch = osBean.getArch();
        String availableProcessors = String.valueOf(runtime.availableProcessors());
        String maxMemory = runtime.maxMemory() / 1024 / 1024 + "MB";
        String totalMemory = runtime.totalMemory() / 1024 / 1024 + "MB";
        String freeMemory = runtime.freeMemory() / 1024 / 1024 + "MB";

        return
            Messenger.f(
                Messenger.c(
                    Messenger.dline(), Messenger.endl(),
                    tr.tr("os", os), Messenger.endl(),
                    tr.tr("os_arch", osArch), Messenger.endl(),
                    tr.tr("available_processors", availableProcessors), Messenger.endl(),
                    tr.tr("max_memory", maxMemory), Messenger.endl(),
                    tr.tr("total_memory", totalMemory), Messenger.endl(),
                    tr.tr("free_memory", freeMemory), Messenger.endl(),
                    Messenger.dline(), Messenger.endl()
                )
            );

    }
}
