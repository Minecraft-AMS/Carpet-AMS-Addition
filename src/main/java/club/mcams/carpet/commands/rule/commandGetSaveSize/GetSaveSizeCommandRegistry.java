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
import club.mcams.carpet.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.util.Objects;

public class GetSaveSizeCommandRegistry {
    private static final Translator translator = new Translator("command.getSaveSize");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("getSaveSize")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetSaveSize))
            .executes(context -> executeGetSaveSize(context.getSource().getPlayerOrThrow()))
        );
    }

    private static int executeGetSaveSize(PlayerEntity player) {
        MinecraftServer server = EntityUtil.getEntityServer(player);
        saveWorld(server, player);
        long size = getFolderSize(getSaveFolder(server));
        String sizeString = formatSize(size);
        player.sendMessage(
            Messenger.s(
                String.format(
                    "§e%s §a§l§n%s",
                    translator.tr("size_msg").getString(),
                    sizeString
                )
            ), false
        );
        return 1;
    }

    private static void saveWorld(MinecraftServer server, PlayerEntity player) {
        if (server != null) {
            String saveAllMessage;
            final String SUCCESS_MSG = translator.tr("save_success_msg").getString();
            final String FAIL_MSG = translator.tr("save_fail_msg").getString();
            //#if MC>=11800
            boolean saveAllSuccess = server.saveAll(false, true, true);
            //#else
            //$$ boolean saveAllSuccess = server.save(false, true, true);
            //#endif
            saveAllMessage = saveAllSuccess ? SUCCESS_MSG : FAIL_MSG;
            player.sendMessage(
                Messenger.s(saveAllMessage).
                setStyle(Style.EMPTY.withColor(Formatting.GRAY)),
                false
            );
        }
    }

    private static File getSaveFolder(MinecraftServer server) {
        return Objects.requireNonNull(server).getSavePath(WorldSavePath.ROOT).toFile();
    }

    private static long getFolderSize(File folder) {
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

    private static String formatSize(long size) {
        double sizeGB = size / (1024.0 * 1024.0 * 1024.0);
        return String.format("%.3f GB", sizeGB);
    }
}