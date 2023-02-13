package club.mcams.carpet.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.List;

public class TextUtil {
    private static MinecraftServer globalServerInstance = null;
    public static void setGlobalServerInstance(MinecraftServer server) {
        globalServerInstance = server;
    }
    private static LiteralText parseText(String text) {
        return new LiteralText(text);
    }
    private static boolean getServerStatus() {
        return globalServerInstance != null && globalServerInstance.isRunning();
    }
    public static void broadcastToAllPlayers(String message) {
        if(getServerStatus()) {
            List<ServerPlayerEntity> serverPlayerEntityList = globalServerInstance.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity serverPlayerEntity : serverPlayerEntityList) {
                serverPlayerEntity.sendMessage(parseText(message), false);
            }
        }
    }
}
