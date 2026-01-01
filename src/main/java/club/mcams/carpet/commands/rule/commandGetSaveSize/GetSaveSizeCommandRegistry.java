/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  A Minecraft Server and contributors
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

package club.mcams.carpet.commands.rule.commandGetSaveSize;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.EntityUtil;
import club.mcams.carpet.utils.Layout;
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.WorldSavePath;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class GetSaveSizeCommandRegistry {
    private static final Translator tr = new Translator("command.getSaveSize");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("getSaveSize")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetSaveSize))
            .executes(context -> executeGetSaveSize(context.getSource(), context.getSource().getPlayer()))
        );
    }

    private static int executeGetSaveSize(ServerCommandSource source, PlayerEntity player) {
        MinecraftServer server = EntityUtil.getEntityServer(player);
        saveWorld(source, server);
        long size = getFolderSize(getSaveFolder(server));
        String sizeString = formatSize(size);

        Messenger.tell(source, Messenger.s(String.format("§e%s §a§l§n%s", tr.tr("size_msg").getString(), sizeString)));

        return 1;
    }

    private static void saveWorld(ServerCommandSource source, MinecraftServer server) {
        if (server != null) {
            //#if MC>=11800
            boolean saveAllSuccess = server.saveAll(false, true, true);
            //#else
            //$$ boolean saveAllSuccess = server.save(false, true, true);
            //#endif
            BaseText message = saveAllSuccess ? tr.tr("save_success_msg") : tr.tr("save_fail_msg");
            Messenger.tell(source, Messenger.f(message, Layout.GRAY));
        }
    }

    @NotNull
    private static File getSaveFolder(MinecraftServer server) {
        return Objects.requireNonNull(server).getSavePath(WorldSavePath.ROOT).toFile();
    }

    private static long getFolderSize(@NotNull File folder) {
        long length = 0;
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    length += file.length();
                } else {
                    length += getFolderSize(file);
                }
            }
        }

        return length;
    }

    @NotNull
    private static String formatSize(long size) {
        double sizeGB = size / (1024.0 * 1024.0 * 1024.0);
        return String.format("%.3f GB", sizeGB);
    }
}