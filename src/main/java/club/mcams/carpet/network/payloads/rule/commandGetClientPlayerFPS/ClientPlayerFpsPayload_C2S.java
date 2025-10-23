package club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS;

import club.mcams.carpet.commands.rule.commandGetClientPlayerFPS.GetClientPlayerFpsRegistry;
import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.utils.NetworkUtil;

import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class ClientPlayerFpsPayload_C2S extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.CLIENT_PLAYER_FPS_C2S.getId();
    private final UUID playerUuid;
    private final int fps;

    private ClientPlayerFpsPayload_C2S(UUID playerUuid, int fps) {
        super(ID);
        this.playerUuid = playerUuid;
        this.fps = fps;
    }

    private ClientPlayerFpsPayload_C2S(PacketByteBuf buf) {
        super(ID);
        this.playerUuid = buf.readUuid();
        this.fps = buf.readInt();
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeUuid(playerUuid);
        buf.writeInt(fps);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnServerThread(() -> GetClientPlayerFpsRegistry.sendFpsResult(this.playerUuid, this.fps));
    }

    public static ClientPlayerFpsPayload_C2S create(UUID playerUuid, int fps) {
        return new ClientPlayerFpsPayload_C2S(playerUuid, fps);
    }

    public static void register() {
        AMS_PayloadManager.register(ID, ClientPlayerFpsPayload_C2S::new);
    }
}
