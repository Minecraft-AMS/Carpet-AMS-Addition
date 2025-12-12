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

package carpetamsaddition.network.payloads.rule.commandSetPlayerPose;

import carpetamsaddition.network.AMS_CustomPayload;
import carpetamsaddition.utils.MinecraftClientUtil;
import carpetamsaddition.utils.PacketByteBufExtras;
import carpetamsaddition.network.AMS_PayloadManager;
import carpetamsaddition.commands.rule.commandSetPlayerPose.SetPlayerPoseCommandRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.player.LocalPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

public class UpdatePlayerPosePayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.UPDATE_PLAYER_POSE_S2C.getId();
    private final Map<UUID, String> poseMap;
    private final UUID targetPlayerUuid;

    private UpdatePlayerPosePayload_S2C(FriendlyByteBuf buf) {
        super(ID);
        this.poseMap = PacketByteBufExtras.readMap(buf, b -> b.readUUID(), FriendlyByteBuf::readUtf);
        this.targetPlayerUuid = buf.readUUID();
    }

    private UpdatePlayerPosePayload_S2C(Map<UUID, String> poseMap, UUID targetPlayerUuid) {
        super(ID);
        this.poseMap = new HashMap<>(poseMap);
        this.targetPlayerUuid = targetPlayerUuid;
    }

    @Override
    protected void writeData(FriendlyByteBuf buf) {
        PacketByteBufExtras.writeMap(buf, this.poseMap, (b, uuid) -> b.writeUUID(uuid), FriendlyByteBuf::writeUtf);
        buf.writeUUID(this.targetPlayerUuid);
    }

    @Override
    public void handle() {
        SetPlayerPoseCommandRegistry.DO_POSE_MAP.clear();
        SetPlayerPoseCommandRegistry.DO_POSE_MAP.putAll(this.poseMap);

        LocalPlayer player = MinecraftClientUtil.getCurrentPlayer();

        if (player.getUUID().equals(this.targetPlayerUuid)) {
            player.setPose(player.getPose());
        }
    }

    public static void register() {
        AMS_PayloadManager.register(ID, UpdatePlayerPosePayload_S2C::new);
    }

    public static UpdatePlayerPosePayload_S2C create(Map<UUID, String> poseMap, UUID targetPlayerUuid) {
        return new UpdatePlayerPosePayload_S2C(poseMap, targetPlayerUuid);
    }
}
