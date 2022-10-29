package club.mcams.carpet.function;

import carpet.CarpetServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class ChunkLoading {
    public static Map<String, Boolean> onlinePlayerMap = new HashMap<>();

    public static void setPlayerInteraction(String playerName, boolean b, boolean online) {
        if (playerFromName(playerName) == null) return;
        if (online) {
            onlinePlayerMap.put(playerName, b);
        }
    }

    public static void onPlayerConnect(PlayerEntity player) {
        String playerName = player.getName().getString();
        setPlayerInteraction(playerName, true, true);
    }

    public static void onPlayerDisconnect(PlayerEntity player) {
        String playerName = player.getName().getString();
        if (onlinePlayerMap.containsKey(playerName)) {
            setPlayerInteraction(playerName, true, false);
            onlinePlayerMap.remove(playerName);
        }
    }

    protected static ServerPlayerEntity playerFromName(String name) {
        return CarpetServer.minecraft_server.getPlayerManager().getPlayer(name);
    }
}