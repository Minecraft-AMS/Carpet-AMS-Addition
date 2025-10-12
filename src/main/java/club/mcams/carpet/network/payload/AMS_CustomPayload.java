/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 */

package club.mcams.carpet.network.payload;

import club.mcams.carpet.utils.IdentifierUtil;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
//#if MC>=12005
//$$ import net.minecraft.network.codec.PacketCodec;
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
//$$ import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
//#else
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import club.mcams.carpet.mixin.network.CustomPayloadC2SPacketAccessor;
import club.mcams.carpet.mixin.network.CustomPayloadS2CPacketAccessor;
import club.mcams.carpet.utils.compat.CustomPayload;
//#endif

import io.netty.buffer.Unpooled;

import java.util.Map;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AMS_CustomPayload implements CustomPayload {
    public static final Identifier CHANNEL_ID = IdentifierUtil.of("carpetamsaddition", "network");
    //#if MC>=12005
    //$$ public static final CustomPayload.Id<AMS_CustomPayload> KEY = new CustomPayload.Id<>(CHANNEL_ID);
    //$$ public static final PacketCodec<PacketByteBuf, AMS_CustomPayload> CODEC = CustomPayload.codecOf(AMS_CustomPayload::write, AMS_CustomPayload::decode);
    //#endif
    private static final Map<String, Function<PacketByteBuf, AMS_CustomPayload>> REGISTRY = new ConcurrentHashMap<>();
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

    protected static String readString(PacketByteBuf buf) {
        //#if MC<11700
        return buf.readString(Short.MAX_VALUE);
        //#else
        //$$ return buf.readString();
        //#endif
    }

    //#if MC>=12005
    //$$ private static AMS_CustomPayload decode(PacketByteBuf buf) {
    //$$     String packetId = buf.readString();
    //$$     Function<PacketByteBuf, AMS_CustomPayload> constructor = REGISTRY.get(packetId);
    //$$     return constructor.apply(buf);
    //$$ }
    //#endif

    //#if MC<12005
    // S2C
    public static AMS_CustomPayload decode(CustomPayloadS2CPacket packet) {
        return getAmsCustomPayload(((CustomPayloadS2CPacketAccessor) packet).getData());
    }

    // C2S
    public static AMS_CustomPayload decode(CustomPayloadC2SPacket packet) {
        return getAmsCustomPayload(((CustomPayloadC2SPacketAccessor) packet).getData());
    }

    private static AMS_CustomPayload getAmsCustomPayload(PacketByteBuf buf) {
        String packetId = readString(buf);
        Function<PacketByteBuf, AMS_CustomPayload> constructor = REGISTRY.get(packetId);
        return constructor.apply(buf);
    }
    //#endif

    public void sendS2CPacket(ServerPlayerEntity player) {
        //#if MC>=12005
        //$$ player.networkHandler.sendPacket(new CustomPayloadS2CPacket(this));
        //#else
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        this.write(buf);
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(CHANNEL_ID, buf);
        player.networkHandler.sendPacket(packet);
        //#endif
    }

    public void sendC2SPacket(ClientPlayerEntity player) {
        //#if MC>=12005
        //$$ player.networkHandler.sendPacket(new CustomPayloadC2SPacket(this));
        //#else
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        this.write(buf);
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(CHANNEL_ID, buf);
        player.networkHandler.sendPacket(packet);
        //#endif
    }

    protected static void register(String packetId, Function<PacketByteBuf, AMS_CustomPayload> constructor) {
        REGISTRY.put(packetId, constructor);
    }

    public abstract void handle();
}
