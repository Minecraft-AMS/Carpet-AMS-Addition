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

package carpetamsaddition.commands.rule.commandAtSomeOnePlayer;

import carpetamsaddition.CarpetAMSAdditionSettings;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.*;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.MutableComponent;

public class AtCommandRegistry {
    private static final Translator tr = new Translator("command.at");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("@")
            .requires(source -> CommandHelper.canUseCommand(source, CarpetAMSAdditionSettings.commandAtSomeOnePlayer))
            .then(Commands.argument("targetPlayer", EntityArgument.player())
            .then(Commands.argument("text", StringArgumentType.greedyString())
            .executes(context -> execute(
                context.getSource().getPlayerOrException(),
                EntityArgument.getPlayer(context, "targetPlayer"),
                StringArgumentType.getString(context, "text")
            ))))
        );
    }

    private static int execute(ServerPlayer sourcePlayer, ServerPlayer targetPlayer, String text) {
        MutableComponent titleText = Messenger.f(Messenger.s(tr.tr("title", PlayerUtil.getName(sourcePlayer))), Layout.AQUA);
        MutableComponent messageText = Messenger.s(String.format("<%s> %s", PlayerUtil.getName(sourcePlayer), text));
        targetPlayer.connection.send(new ClientboundSetTitleTextPacket(titleText));
        Messenger.sendServerMessage(MinecraftServerUtil.getServer(), messageText);
        EntityUtil.getEntityWorld(targetPlayer).playSound(null, targetPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        Messenger.sendServerMessage(
            MinecraftServerUtil.getServer(),
            Messenger.f(Messenger.s(String.format("%s @ %s", PlayerUtil.getName(sourcePlayer), PlayerUtil.getName(targetPlayer))), Layout.GRAY)
        );
        return 1;
    }
}
