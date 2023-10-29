/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
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

package club.mcams.carpet.commands;

import club.mcams.carpet.AmsServerSettings;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.literal;

public class anvilInteractionDisabledCommandRegistry {
    public static boolean anvilInteractionDisabledSwitch = false;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
            dispatcher.register(literal("anvilInteractionDisabledSwitch")
                    .requires(source -> source.hasPermissionLevel(0))
                    .executes(context -> {
                        if(AmsServerSettings.anvilInteractionDisabled) {
                            anvilInteractionDisabledSwitch = !anvilInteractionDisabledSwitch;
                            if(anvilInteractionDisabledSwitch) {
                                //#if MC>11800
                                //$$ Text disableMessage = Text.literal("[ Anvil Interaction Disable ]")
                                //#else
                                Text disableMessage = new LiteralText("[ Anvil Interaction Disable ]")
                                        //#endif
                                        .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000)));
                                context.getSource().getPlayer().sendMessage(disableMessage, true);
                            } else {
                                //#if MC>11800
                                //$$ Text enableMessage = Text.literal("[ Anvil Interaction Enable ]")
                                //#else
                                Text enableMessage = new LiteralText("[ Anvil Interaction Enable ]")
                                        //#endif
                                        .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00)));
                                context.getSource().getPlayer().sendMessage(enableMessage, true);
                            }
                        } else {
                            //#if MC>11800
                            //$$ Text notEnableMessage = Text.literal("[ Carpet Rule anvilInteractionDisabled Not enabled ]")
                            //#else
                            Text notEnableMessage = new LiteralText("[ Carpet Rule anvilInteractionDisabled Not enabled ]")
                                    //#endif
                                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFC5454)));
                            context.getSource().getPlayer().sendMessage(notEnableMessage, true);
                        }
                        return 1;
                    }));
    }
}