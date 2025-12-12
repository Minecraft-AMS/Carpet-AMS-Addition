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

package carpetamsaddition.commands.rule.commandGetSaveSize;

import carpetamsaddition.AmsServerSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.util.Objects;

public class GetSaveSizeCommandRegistry {
    private static final Translator translator = new Translator("command.getSaveSize");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("getSaveSize")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandGetSaveSize))
            .executes(context -> executeGetSaveSize(context.getSource().getPlayerOrException()))
        );
    }

    private static int executeGetSaveSize(Player player) {
        MinecraftServer server = EntityUtil.getEntityServer(player);
        saveWorld(server, player);
        long size = getFolderSize(getSaveFolder(server));
        String sizeString = formatSize(size);
        player.displayClientMessage(
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

    private static void saveWorld(MinecraftServer server, Player player) {
        if (server != null) {
            String saveAllMessage;
            final String SUCCESS_MSG = translator.tr("save_success_msg").getString();
            final String FAIL_MSG = translator.tr("save_fail_msg").getString();
            //#if MC>=11800
            boolean saveAllSuccess = server.saveEverything(false, true, true);
            //#else
            //$$ boolean saveAllSuccess = server.save(false, true, true);
            //#endif
            saveAllMessage = saveAllSuccess ? SUCCESS_MSG : FAIL_MSG;
            player.displayClientMessage(
                Messenger.s(saveAllMessage).
                setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)),
                false
            );
        }
    }

    private static File getSaveFolder(MinecraftServer server) {
        return Objects.requireNonNull(server).getWorldPath(LevelResource.ROOT).toFile();
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