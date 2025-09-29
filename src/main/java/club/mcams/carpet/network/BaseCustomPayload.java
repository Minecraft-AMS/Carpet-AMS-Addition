/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 */

package club.mcams.carpet.network;

import io.netty.buffer.Unpooled;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.util.Identifier;

//#if MC>=12005
//$$ import net.minecraft.network.codec.PacketCodec;
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
//#else
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
//#endif

import java.util.function.Function;

public abstract class BaseCustomPayload implements CustomPayload {
    protected final Identifier channelId;

    protected BaseCustomPayload(Identifier channelId) {
        this.channelId = channelId;
    }

    //#if MC<12005
    @Override
    public Identifier getId() {
        return channelId;
    }
    //#endif

    //#if MC>=12005
    //$$ public Object getRawPacket() {
    //$$     return new CustomPayloadS2CPacket((CustomPayload) this);
    //$$ }
    //#endif

    public void sendToPlayer(ServerPlayerEntity player) {
        //#if MC>=12005
        //$$ player.networkHandler.sendPacket(new CustomPayloadS2CPacket((CustomPayload) this));
        //#else
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        this.write(buf);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(this.channelId, buf);
        player.networkHandler.sendPacket(packet);
        //#endif
    }


    public static <T extends BaseCustomPayload> T create(Identifier channelId, PacketByteBuf buf, Function<PacketByteBuf, T> constructor) {
        return constructor.apply(buf);
    }
}