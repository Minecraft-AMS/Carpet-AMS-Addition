/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
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

package carpetamsaddition.commands.rule.commandPlayerNoNetherPortalTeleport;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.Layout;
import carpetamsaddition.utils.messenger.Messenger;
import carpetamsaddition.utils.PlayerUtil;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerNoNetherPortalTeleportCommand implements AmsCommand {
    public static final Set<UUID> NO_NETHER_PORTAL_TELEPORT_SET = new LinkedHashSet<>();
    public static boolean isGlobalMode;
    private static final Translator tr = new Translator("command.playerNoNetherPortalTeleport");
    @Override
    public void define(CommandBuilder command) {
        command.name("playerNoNetherPortalTeleport")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandPlayerNoNetherPortalTeleport)
            .route("globalMode").executes(c -> showGlobalModeState(c.source()))
            .route("globalMode", Arguments.bool("bool")).executes(c -> setGlobalMode(c.source(), c.get(Arguments.bool("bool"))))
            .route("add", Arguments.player("player")).executes(c -> add(c.source(), c.get(Arguments.player("player"))))
            .route("remove", Arguments.player("player")).executes(c -> remove(c.source(), c.get(Arguments.player("player"))))
            .route("clear").executes(c -> clear(c.source()))
            .route("list").executes(c -> list(c.source()))
            .route("help").executes(c -> help(c.source()));
    }

    private static int add(CommandSourceStack source, Player player) {
        if (!NO_NETHER_PORTAL_TELEPORT_SET.contains(PlayerUtil.getPlayerUUID(player))) {
            NO_NETHER_PORTAL_TELEPORT_SET.add(PlayerUtil.getPlayerUUID(player));
            Messenger.tell(source, Messenger.f(tr.tr("add_success", PlayerUtil.getName(player)), Layout.GREEN));
            return 1;
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("add_fail", PlayerUtil.getName(player)), Layout.YELLOW));
            return 0;
        }
    }

    private static int remove(CommandSourceStack source, Player player) {
        if (NO_NETHER_PORTAL_TELEPORT_SET.contains(PlayerUtil.getPlayerUUID(player))) {
            NO_NETHER_PORTAL_TELEPORT_SET.remove(PlayerUtil.getPlayerUUID(player));
            Messenger.tell(source, Messenger.f(tr.tr("remove_success", PlayerUtil.getName(player)), Layout.GREEN));
            return 1;
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("remove_fail", PlayerUtil.getName(player)), Layout.YELLOW));
            return 0;
        }
    }

    private static int clear(CommandSourceStack source) {
        if (NO_NETHER_PORTAL_TELEPORT_SET.isEmpty()) {
            Messenger.tell(source, Messenger.f(tr.tr("clear_fail"), Layout.YELLOW));
            return 0;
        } else {
            NO_NETHER_PORTAL_TELEPORT_SET.clear();
            Messenger.tell(source, Messenger.f(tr.tr("clear_success"), Layout.GREEN));
            return 1;
        }
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(source, Messenger.f(tr.tr("list_title"), Layout.AQUA));
        Messenger.tell(source, Messenger.f((MutableComponent) Messenger.dline(), Layout.AQUA));

        for (UUID player : NO_NETHER_PORTAL_TELEPORT_SET) {
            Messenger.tell(source, Messenger.f(Messenger.s(PlayerUtil.getName(player)), Layout.AQUA));
        }

        return 1;
    }

    private static int setGlobalMode(CommandSourceStack source, boolean enabled) {
        isGlobalMode = enabled;

        if (isGlobalMode) {
            Messenger.tell(source, Messenger.f(tr.tr("globalMode_enable"), Layout.GREEN));
        } else {
            Messenger.tell(source, Messenger.f(tr.tr("globalMode_disable"), Layout.GREEN));
        }

        return 1;
    }

    private static int showGlobalModeState(CommandSourceStack source) {
        String globalModeState = isGlobalMode ? "true" : "false";
        Messenger.tell(source, Messenger.f(tr.tr("globalMode_state", globalModeState), Layout.AQUA));
        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(
            source, Messenger.f(Messenger.c(
                tr.tr("help.globalMode"), Messenger.endl(),
                tr.tr("help.globalModeState"), Messenger.endl(),
                tr.tr("help.add"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.clear"), Messenger.endl(),
                tr.tr("help.list")
            ), Layout.GRAY)
        );

        return 1;
    }
}
