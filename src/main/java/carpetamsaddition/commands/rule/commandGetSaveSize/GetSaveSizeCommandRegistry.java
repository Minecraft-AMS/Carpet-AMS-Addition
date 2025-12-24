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

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.EntityUtil;
import carpetamsaddition.utils.Messenger;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

public class GetSaveSizeCommandRegistry {
    private static final Translator tr = new Translator("command.getSaveSize");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("getSaveSize")
            .requires(s -> CommandHelper.canUseCommand(s, CarpetAMSAdditionSettings.commandGetSaveSize))
            .executes(c -> executeGetSaveSize(c.getSource(), c.getSource().getPlayer()))
        );
    }

    private static int executeGetSaveSize(CommandSourceStack source, Player player) {
        MinecraftServer server = EntityUtil.getEntityServer(player);
        saveWorld(source, server);
        long size = getFolderSize(getSaveFolder(server));
        String sizeString = formatSize(size);

        Messenger.tell(
            source,
            Messenger.s(
                String.format(
                    "§e%s §a§l§n%s",
                    tr.tr("size_msg").getString(),
                    sizeString
                )
            )
        );

        return 1;
    }

    private static void saveWorld(CommandSourceStack source, MinecraftServer server) {
        if (server != null) {
            boolean saveAllSuccess = server.saveEverything(false, true, true);
            MutableComponent message = saveAllSuccess ? tr.tr("save_success_msg") : tr.tr("save_fail_msg");
            Messenger.tell(source, Messenger.f(message, ChatFormatting.GRAY));
        }
    }

    @NotNull
    private static File getSaveFolder(MinecraftServer server) {
        return Objects.requireNonNull(server).getWorldPath(LevelResource.ROOT).toFile();
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