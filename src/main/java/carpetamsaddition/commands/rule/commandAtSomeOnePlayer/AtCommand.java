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
import carpetamsaddition.api.command.AmsCommand;
import carpetamsaddition.api.command.Arguments;
import carpetamsaddition.api.command.CommandBuilder;
import carpetamsaddition.translations.Translator;
import carpetamsaddition.utils.*;

import carpetamsaddition.utils.messenger.Messenger;
//#if MC<11700
//$$ import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket;
//#else
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
//#endif
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.MutableComponent;

public class AtCommand implements AmsCommand {
    private static final Translator tr = new Translator("command.at");
    @Override
    public void define(CommandBuilder command) {
        command.name("@")
            .carpetRule(() -> CarpetAMSAdditionSettings.commandAtSomeOnePlayer)
            .route(Arguments.player("targetPlayer"), Arguments.greedyString("text"))
            .executes(c -> execute(c.player(), c.get(Arguments.player("targetPlayer")), c.get(Arguments.greedyString("text"))));
    }

    private static int execute(ServerPlayer sourcePlayer, ServerPlayer targetPlayer, String text) {
        MutableComponent titleText = Messenger.f(tr.tr("title", PlayerUtil.getName(sourcePlayer)), Layout.AQUA);
        MutableComponent messageText = Messenger.s(String.format("<%s> %s", PlayerUtil.getName(sourcePlayer), text));
        //#if MC<11700
        //$$ targetPlayer.connection.send(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.TITLE, titleText));
        //#else
        targetPlayer.connection.send(new ClientboundSetTitleTextPacket(titleText));
        //#endif
        Messenger.sendServerMessage(MinecraftServerUtil.getServer(), messageText);
        EntityUtil.getEntityWorld(targetPlayer).playSound(null, targetPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        Messenger.sendServerMessage(
            MinecraftServerUtil.getServer(),
            Messenger.f(Messenger.s(String.format("%s @ %s", PlayerUtil.getName(sourcePlayer), PlayerUtil.getName(targetPlayer))), Layout.GRAY)
        );
        return 1;
    }
}
