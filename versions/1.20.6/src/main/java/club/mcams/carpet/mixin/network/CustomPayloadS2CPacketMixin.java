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

package club.mcams.carpet.mixin.network;

import club.mcams.carpet.network.payload.AMS_CustomPayload;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import top.byteeeee.annotationtoolbox.annotation.GameVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@GameVersion(version = "Minecraft >= 1.20.5")
@Mixin(CustomPayloadS2CPacket.class)
public abstract class CustomPayloadS2CPacketMixin {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;"
        )
    )
    private static List<?> registerAMSPayloadS2C(List<CustomPayload.Type<?, ?>> types) {
        types = new ArrayList<>(types);
        types.add(new CustomPayload.Type<>(AMS_CustomPayload.KEY, AMS_CustomPayload.CODEC));
        return Collections.unmodifiableList(types);
    }
}
