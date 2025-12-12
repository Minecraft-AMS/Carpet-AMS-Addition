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

package carpetamsaddition.logging.logger;

import carpetamsaddition.AmsServer;
import carpetamsaddition.logging.AbstractHUDLogger;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Messenger;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.MutableComponent;

import java.time.Duration;
import java.time.Instant;

public class ServerRuntimeHUDLogger extends AbstractHUDLogger {
    private static final Translator translator = new Translator("logger.serverRuntime");
    public static final String NAME = "serverRuntime";
    private static final ServerRuntimeHUDLogger INSTANCE = new ServerRuntimeHUDLogger();

    private ServerRuntimeHUDLogger() {
        super(NAME);
    }

    public static ServerRuntimeHUDLogger getInstance() {
        return INSTANCE;
    }

    @Override
    public MutableComponent[] onHudUpdate(String option, Player playerEntity) {
        Instant currentTime = Instant.now();
        Instant serverStartTime = Instant.ofEpochMilli(AmsServer.serverStartTimeMillis);
        Duration elapsedDuration = Duration.between(serverStartTime, currentTime);
        long elapsedSeconds = elapsedDuration.getSeconds();
        long hours = elapsedSeconds / 3600;
        long remainingSeconds = elapsedSeconds % 3600;
        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;
        String formattedTime = String.format("%02d : %02d : %02d", hours, minutes, seconds);
        return new MutableComponent[]{
            Messenger.c(
                String.format("q %s ", translator.tr("server_runtime").getString()),
                String.format("c %s", formattedTime)
            )
        };
    }
}