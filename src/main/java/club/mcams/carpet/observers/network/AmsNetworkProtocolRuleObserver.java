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

package club.mcams.carpet.observers.network;

import carpet.api.settings.CarpetRule;

import club.mcams.carpet.utils.NetworkUtil;
import club.mcams.carpet.settings.RuleObserver;
import club.mcams.carpet.utils.MinecraftServerUtil;
import club.mcams.carpet.network.payloads.handshake.RequestHandShakeS2CPayload;

import net.minecraft.server.command.ServerCommandSource;

public class AmsNetworkProtocolRuleObserver extends RuleObserver<Boolean> {
    @Override
    public void onValueChange(ServerCommandSource source, CarpetRule<Boolean> rule, Boolean oldValue, Boolean newValue) {
        if (MinecraftServerUtil.serverIsRunning()) {
            NetworkUtil.forcedBroadcastDataPack(MinecraftServerUtil.getServer(), RequestHandShakeS2CPayload.create());
        }
    }
}
