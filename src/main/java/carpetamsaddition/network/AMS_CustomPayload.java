/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025 A Minecraft Server and contributors
 */

package carpetamsaddition.network;

import carpetamsaddition.utils.IdentifierUtil;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.minecraft.resources.Identifier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.NotNull;

public abstract class AMS_CustomPayload implements CustomPacketPayload {
    public static final Identifier CHANNEL_ID = IdentifierUtil.of("carpetamsaddition", "network/v1");
    public static final CustomPacketPayload.Type<@NotNull AMS_CustomPayload> KEY = new CustomPacketPayload.Type<>(CHANNEL_ID);
    public static final StreamCodec<@NotNull FriendlyByteBuf, @NotNull AMS_CustomPayload> CODEC = CustomPacketPayload.codec(AMS_CustomPayload::write, AMS_PayloadCodec::decodePayload);
    private final String packetId;

    protected AMS_CustomPayload(String packetId) {
        this.packetId = packetId;
    }

    public String getPacketId() {
        return this.packetId;
    }

    @Override
    public CustomPacketPayload.@NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return KEY;
    }

    public final void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.packetId);
        writeData(buf);
    }

    protected abstract void writeData(FriendlyByteBuf buf);

    protected abstract void handle();

    public final void sendC2SPacket(LocalPlayer player) {
        AMS_PayloadSender.c2s(this, player);
    }

    public final void sendS2CPacket(ServerPlayer player) {
        AMS_PayloadSender.s2c(this, player);
    }
}
