/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 */

package club.mcams.carpet.network;

import club.mcams.carpet.utils.IdentifierUtil;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
//#if MC>=12005
//$$ import net.minecraft.network.codec.PacketCodec;
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
//#else
import club.mcams.carpet.utils.compat.CustomPayload;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
//#endif

import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AMS_CustomPayload implements CustomPayload {
    public static final Identifier CHANNEL_ID = IdentifierUtil.of("carpetamsaddition", "network");
    //#if MC>=12005
    //$$ public static final CustomPayload.Id<AMS_CustomPayload> KEY = new CustomPayload.Id<>(CHANNEL_ID);
    //$$ public static final PacketCodec<PacketByteBuf, AMS_CustomPayload> CODEC = CustomPayload.codecOf(AMS_CustomPayload::write, AMS_CustomPayload::read);
    //#endif
    private static final Map<String, Function<PacketByteBuf, AMS_CustomPayload>> REGISTRY = new HashMap<>();
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

    //#if MC>=12005
    //$$ public static AMS_CustomPayload read(PacketByteBuf buf) {
    //$$     String packetId = buf.readString();
    //$$     Function<PacketByteBuf, AMS_CustomPayload> constructor = REGISTRY.get(packetId);
    //$$     if (constructor == null) {
    //$$         throw new IllegalArgumentException("Unknown packet id: " + packetId);
    //$$     }
    //$$     return constructor.apply(buf);
    //$$ }
    //#endif

    public static void register(String packetId, Function<PacketByteBuf, AMS_CustomPayload> constructor) {
        REGISTRY.put(packetId, constructor);
    }

    //#if MC<12005
    public static AMS_CustomPayload decode(CustomPayloadS2CPacket packet) {
        PacketByteBuf buf = packet.getData();
        String packetId = buf.readString();
        Function<PacketByteBuf, AMS_CustomPayload> constructor = REGISTRY.get(packetId);
        if (constructor == null) {
            throw new IllegalArgumentException("Unknown packet id: " + packetId);
        }
        return constructor.apply(buf);
    }
    //#endif

    public void sendToPlayer(ServerPlayerEntity player) {
        //#if MC>=12005
        //$$ player.networkHandler.sendPacket(new CustomPayloadS2CPacket(this));
        //#else
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        this.write(buf);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(CHANNEL_ID, buf);
        player.networkHandler.sendPacket(packet);
        //#endif
    }
}
