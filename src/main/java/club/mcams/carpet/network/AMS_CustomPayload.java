/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 */

package club.mcams.carpet.network;

import club.mcams.carpet.utils.IdentifierUtil;

//#if MC>=12005
//$$ import net.minecraft.network.codec.PacketCodec;
//$$ import net.minecraft.network.packet.CustomPayload;
//#else
import club.mcams.carpet.utils.compat.CustomPayload;
//#endif

import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AMS_CustomPayload implements CustomPayload {
    public static final Identifier CHANNEL_ID = IdentifierUtil.of("carpetamsaddition", "network/v1");
    //#if MC>=12005
    //$$ public static final CustomPayload.Id<AMS_CustomPayload> KEY = new CustomPayload.Id<>(CHANNEL_ID);
    //$$     public static final PacketCodec<PacketByteBuf, AMS_CustomPayload> CODEC = CustomPayload.codecOf(AMS_CustomPayload::write, AMS_PayloadCodec::decodePayload);
    //#endif
    private final String packetId;

    protected AMS_CustomPayload(String packetId) {
        this.packetId = packetId;
    }

    //#if MC>=12005
    //$$ @Override
    //$$ public CustomPayload.Id<? extends CustomPayload> getId() {
    //$$     return KEY;
    //$$ }
    //#endif

    //#if MC<12005
    @Override
    //#endif
    public final void write(PacketByteBuf buf) {
        buf.writeString(this.packetId);
        writeData(buf);
    }

    protected abstract void writeData(PacketByteBuf buf);

    protected abstract void handle();

    public final void sendC2SPacket(ClientPlayerEntity player) {
        AMS_PayloadSender.c2s(this, player);
    }

    public final void sendS2CPacket(ServerPlayerEntity player) {
        AMS_PayloadSender.s2c(this, player);
    }
}
