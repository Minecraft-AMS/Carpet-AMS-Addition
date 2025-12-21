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

package club.mcams.carpet.commands.rule.commandPlayerNoNetherPortalTeleport;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.translations.Translator;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.PlayerUtil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PlayerNoNetherPortalTeleportRegistry {
    public static final Set<UUID> NO_NETHER_PORTAL_TELEPORT_SET = new LinkedHashSet<>();
    public static boolean isGlobalMode;
    private static final Translator tr = new Translator("command.playerNoNetherPortalTeleport");

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("playerNoNetherPortalTeleport")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandPlayerNoNetherPortalTeleport))
            // global mode
            .then(literal("globalMode")
            .executes(c -> showGlobalModeState(c.getSource()))
            .then(argument("bool", BoolArgumentType.bool())
            .executes(c -> setGlobalMode(c, c.getSource()))))

            // add
            .then(literal("add")
            .then(argument("player", EntityArgumentType.player())
            .executes(c -> add(c.getSource(), EntityArgumentType.getPlayer(c, "player")))))

            // remove
            .then(literal("remove")
            .then(argument("player", EntityArgumentType.player())
            .executes(c -> remove(c.getSource(), EntityArgumentType.getPlayer(c, "player")))))

            // clear
            .then(literal("clear")
            .executes(c -> clear(c.getSource())))

            // list
            .then(literal("list").executes(c -> list(c.getSource())))

            // help
            .then(literal("help").executes(c -> help(c.getSource())))
        );
    }

    private static int add(ServerCommandSource source, PlayerEntity player) {
        if (!NO_NETHER_PORTAL_TELEPORT_SET.contains(PlayerUtil.getPlayerUUID(player))) {
            NO_NETHER_PORTAL_TELEPORT_SET.add(PlayerUtil.getPlayerUUID(player));
            Messenger.tell(source, (BaseText) tr.tr("add_success", PlayerUtil.getName(player)).formatted(Formatting.GREEN));
            return 1;
        } else {
            Messenger.tell(source, (BaseText) tr.tr("add_fail", PlayerUtil.getName(player)).formatted(Formatting.YELLOW));
            return 0;
        }
    }

    private static int remove(ServerCommandSource source, PlayerEntity player) {
        if (NO_NETHER_PORTAL_TELEPORT_SET.contains(PlayerUtil.getPlayerUUID(player))) {
            NO_NETHER_PORTAL_TELEPORT_SET.remove(PlayerUtil.getPlayerUUID(player));
            Messenger.tell(source, (BaseText) tr.tr("remove_success", PlayerUtil.getName(player)).formatted(Formatting.GREEN));
            return 1;
        } else {
            Messenger.tell(source, (BaseText) tr.tr("remove_fail", PlayerUtil.getName(player)).formatted(Formatting.YELLOW));
            return 0;
        }
    }

    private static int clear(ServerCommandSource source) {
        if (NO_NETHER_PORTAL_TELEPORT_SET.isEmpty()) {
            Messenger.tell(source, (BaseText) tr.tr("clear_fail").formatted(Formatting.YELLOW));
            return 0;
        } else {
            NO_NETHER_PORTAL_TELEPORT_SET.clear();
            Messenger.tell(source, (BaseText) tr.tr("clear_success").formatted(Formatting.GREEN));
            return 1;
        }
    }

    private static int list(ServerCommandSource source) {
        Messenger.tell(source, (BaseText) tr.tr("list_title").formatted(Formatting.AQUA));
        Messenger.tell(source, Messenger.s("======================================", Formatting.AQUA));

        for (UUID uuid : NO_NETHER_PORTAL_TELEPORT_SET) {
            Messenger.tell(source, Messenger.s(PlayerUtil.getName(PlayerUtil.getServerPlayerEntity(uuid)), Formatting.AQUA));
        }

        return 1;
    }

    private static int setGlobalMode(CommandContext<?> context, ServerCommandSource source) {
        isGlobalMode = BoolArgumentType.getBool(context, "bool");

        if (isGlobalMode) {
            Messenger.tell(source, (BaseText) tr.tr("globalMode_enable").formatted(Formatting.GREEN));
        } else {
            Messenger.tell(source, (BaseText) tr.tr("globalMode_disable").formatted(Formatting.GREEN));
        }

        return 1;
    }

    private static int showGlobalModeState(ServerCommandSource source) {
        String globalModeState = isGlobalMode ? "true" : "false";
        Messenger.tell(source, (BaseText) tr.tr("globalMode_state", globalModeState).formatted(Formatting.AQUA));
        return 1;
    }

    private static int help(ServerCommandSource source) {
        Messenger.tell(
            source, (BaseText) Messenger.c(
                tr.tr("help.globalMode"), Messenger.endl(),
                tr.tr("help.globalModeState"), Messenger.endl(),
                tr.tr("help.add"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.clear"), Messenger.endl(),
                tr.tr("help.list")
            ).formatted(Formatting.GRAY)
        );

        return 1;
    }
}
