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
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.CommandHelper;
import carpetamsaddition.utils.Messenger;
import carpetamsaddition.utils.PlayerUtil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedHashSet;
import java.util.Set;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class PlayerNoNetherPortalTeleportRegistry {
    public static final Set<Player> NO_NETHER_PORTAL_TELEPORT_SET = new LinkedHashSet<>();
    public static boolean isGlobalMode;
    private static final Translator tr = new Translator("command.playerNoNetherPortalTeleport");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("playerNoNetherPortalTeleport")
            .requires((player) -> CommandHelper.canUseCommand(player, CarpetAMSAdditionSettings.commandPlayerNoNetherPortalTeleport))
            // global mode
            .then(literal("globalMode")
            .executes(c -> showGlobalModeState(c.getSource()))
            .then(argument("bool", BoolArgumentType.bool())
            .executes(c -> setGlobalMode(c, c.getSource()))))

            // add
            .then(literal("add")
            .then(argument("player", EntityArgument.player())
            .executes(c -> add(c.getSource(), EntityArgument.getPlayer(c, "player")))))

            // remove
            .then(literal("remove")
            .then(argument("player", EntityArgument.player())
            .executes(c -> remove(c.getSource(), EntityArgument.getPlayer(c, "player")))))

            // clear
            .then(literal("clear")
            .executes(c -> clear(c.getSource())))

            // list
            .then(literal("list").executes(c -> list(c.getSource())))

            // help
            .then(literal("help").executes(c -> help(c.getSource())))
        );
    }

    private static int add(CommandSourceStack source, Player player) {
        if (!NO_NETHER_PORTAL_TELEPORT_SET.contains(player)) {
            NO_NETHER_PORTAL_TELEPORT_SET.add(player);
            Messenger.tell(source, tr.tr("add_success", PlayerUtil.getName(player)).withStyle(ChatFormatting.GREEN));
            return 1;
        } else {
            Messenger.tell(source, tr.tr("add_fail", PlayerUtil.getName(player)).withStyle(ChatFormatting.YELLOW));
            return 0;
        }
    }

    private static int remove(CommandSourceStack source, Player player) {
        if (NO_NETHER_PORTAL_TELEPORT_SET.contains(player)) {
            NO_NETHER_PORTAL_TELEPORT_SET.remove(player);
            Messenger.tell(source, tr.tr("remove_success", PlayerUtil.getName(player)).withStyle(ChatFormatting.GREEN));
            return 1;
        } else {
            Messenger.tell(source, tr.tr("remove_fail", PlayerUtil.getName(player)).withStyle(ChatFormatting.YELLOW));
            return 0;
        }
    }

    private static int clear(CommandSourceStack source) {
        if (NO_NETHER_PORTAL_TELEPORT_SET.isEmpty()) {
            Messenger.tell(source, tr.tr("clear_fail").withStyle(ChatFormatting.YELLOW));
            return 0;
        } else {
            NO_NETHER_PORTAL_TELEPORT_SET.clear();
            Messenger.tell(source, tr.tr("clear_success").withStyle(ChatFormatting.GREEN));
            return 1;
        }
    }

    private static int list(CommandSourceStack source) {
        Messenger.tell(source, tr.tr("list_title").withStyle(ChatFormatting.AQUA));
        Messenger.tell(source, Messenger.s("======================================", ChatFormatting.AQUA));

        for (Player player : NO_NETHER_PORTAL_TELEPORT_SET) {
            Messenger.tell(source, Messenger.s(PlayerUtil.getName(player), ChatFormatting.AQUA));
        }

        return 1;
    }

    private static int setGlobalMode(CommandContext<?> context, CommandSourceStack source) {
        isGlobalMode = BoolArgumentType.getBool(context, "bool");

        if (isGlobalMode) {
            Messenger.tell(source, tr.tr("globalMode_enable").withStyle(ChatFormatting.GREEN));
        } else {
            Messenger.tell(source, tr.tr("globalMode_disable").withStyle(ChatFormatting.GREEN));
        }

        return 1;
    }

    private static int showGlobalModeState(CommandSourceStack source) {
        String globalModeState = isGlobalMode ? "true" : "false";
        Messenger.tell(source, tr.tr("globalMode_state", globalModeState).withStyle(ChatFormatting.AQUA));
        return 1;
    }

    private static int help(CommandSourceStack source) {
        Messenger.tell(
            source, Messenger.c(
                tr.tr("help.globalMode"), Messenger.endl(),
                tr.tr("help.globalModeState"), Messenger.endl(),
                tr.tr("help.add"), Messenger.endl(),
                tr.tr("help.remove"), Messenger.endl(),
                tr.tr("help.clear"), Messenger.endl(),
                tr.tr("help.list")
            ).withStyle(ChatFormatting.GRAY)
        );

        return 1;
    }
}
