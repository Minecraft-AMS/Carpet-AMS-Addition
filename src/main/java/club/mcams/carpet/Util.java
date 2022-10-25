package club.mcams.carpet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.List;

public class Util {
    private static MinecraftServer globalServerInstance = null;

    public static void setGlobalServer(MinecraftServer server) {
        globalServerInstance = server;
    }

    public static MinecraftServer getGlobalServer() {
        if (globalServerInstance != null && globalServerInstance.isRunning()) {
            return globalServerInstance;
        } else {
            return null;
        }
    }

    public static void broadcastToAllPlayers(String message) {
        broadcastToAllPlayers(globalServerInstance, message);
    }

    public static void broadcastToAllPlayers(MinecraftServer server, String message) {
        if (server != null && server.isRunning()) {
            List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : playerList) {
                player.sendMessage(new LiteralText(message), false);
            }
        }
    }

    public static <E> String list2Str(List<E> list) {
        StringBuilder builder = new StringBuilder("[");
        list.forEach(element -> builder.append(element.toString()).append(","));
        builder.deleteCharAt(builder.length() - 1).append("]");
        return builder.toString();
    }
}