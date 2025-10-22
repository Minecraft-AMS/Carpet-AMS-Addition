package club.mcams.carpet.network.payloads.rule.commandGetClientPlayerFPS;

import club.mcams.carpet.network.AMS_CustomPayload;
import club.mcams.carpet.network.AMS_PayloadManager;
import club.mcams.carpet.utils.MinecraftClientUtil;
import club.mcams.carpet.utils.NetworkUtil;

import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class ClientPlayerFpsPayload_S2C extends AMS_CustomPayload {
    private static final String ID = AMS_PayloadManager.PacketId.CLIENT_PLAYER_FPS_S2C.getId();
    private final UUID targetPlayerUuid;

    private ClientPlayerFpsPayload_S2C(UUID targetPlayerUuid) {
        super(ID);
        this.targetPlayerUuid = targetPlayerUuid;
    }

    private ClientPlayerFpsPayload_S2C(PacketByteBuf buf) {
        super(ID);
        this.targetPlayerUuid = buf.readUuid();
    }

    @Override
    protected void writeData(PacketByteBuf buf) {
        buf.writeUuid(targetPlayerUuid);
    }

    @Override
    public void handle() {
        NetworkUtil.executeOnClientThread(() ->
            NetworkUtil.sendC2SPacketIfSupport(
                MinecraftClientUtil.getCurrentPlayer(),
                ClientPlayerFpsPayload_C2S.create(targetPlayerUuid, MinecraftClientUtil.getClientFps())
            )
        );
    }

    public static ClientPlayerFpsPayload_S2C create(UUID targetPlayerUuid) {
        return new ClientPlayerFpsPayload_S2C(targetPlayerUuid);
    }

    public static void register() {
        AMS_PayloadManager.register(ID, ClientPlayerFpsPayload_S2C::new);
    }
}
