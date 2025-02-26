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

package club.mcams.carpet.commands.rule.commandAtSomeOnePlayer;

import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.utils.CommandHelper;
import club.mcams.carpet.utils.Messenger;
import club.mcams.carpet.utils.MinecraftServerUtil;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

public class AtCommandRegistry {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("@")
            .requires(source -> CommandHelper.canUseCommand(source, AmsServerSettings.commandAtSomeOnePlayer))
            .then(CommandManager.argument("targetPlayer", EntityArgumentType.player()).then(CommandManager.argument("text", StringArgumentType.greedyString())
            .executes(context -> execute(
                context.getSource().getPlayer(),
                EntityArgumentType.getPlayer(context, "targetPlayer"),
                StringArgumentType.getString(context, "text")
            ))))
        );
    }

    private static int execute(ServerPlayerEntity sourcePlayer, ServerPlayerEntity targetPlayer, String text) {
        final BaseText titleText = Messenger.s(sourcePlayer.getGameProfile().getName() + " @ 了你");
        BaseText messageText = Messenger.s(String.format("<%s> %s", sourcePlayer.getGameProfile().getName(), text));
        //#if MC<11700
        //$$ targetPlayer.networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.TITLE, titleText.formatted(Formatting.AQUA)));
        //#else
        targetPlayer.networkHandler.sendPacket(new TitleS2CPacket(titleText.formatted(Formatting.AQUA)));
        //#endif
        targetPlayer.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        //#if MC>=11900
        //$$ MinecraftServerUtil.getServer().getPlayerManager().broadcast(messageText, false);
        //#else
        MinecraftServerUtil.getServer().getPlayerManager().broadcast(messageText, MessageType.CHAT, sourcePlayer.getUuid());
        //#endif
        return 1;
    }
}
