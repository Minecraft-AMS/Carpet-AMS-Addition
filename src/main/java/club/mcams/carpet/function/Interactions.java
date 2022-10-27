package club.mcams.carpet.function;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import carpet.CarpetServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class Interactions {
    public static Map<String, Set<String>> onlinePlayerMap = new HashMap<>();
    public static Map<String, Set<String>> offlinePlayerMap = new HashMap<>();
    public static Set<String> interactionList = Set.of(
            "all",
            "chunkloading",
            "entities",
            "blocks",
            "updates"
    );

    public static void setPlayerInteraction(String playerName, String interactionName, boolean b, boolean online) {
        if (playerFromName(playerName) == null) return;
        if (interactionName.equals("all"))
            for (String x : Interactions.interactionList)
                if (!x.equals("all")) {
                    if (online) {
                        if (!onlinePlayerMap.containsKey(playerName)) onlinePlayerMap.put(playerName, new HashSet<>());
                        if (b) onlinePlayerMap.get(playerName).add(x);
                        else onlinePlayerMap.get(playerName).remove(x);
                        if (onlinePlayerMap.get(playerName).size() == 0) onlinePlayerMap.remove(playerName);
                    } else {
                        if (!offlinePlayerMap.containsKey(playerName))
                            offlinePlayerMap.put(playerName, new HashSet<>());
                        if (b) offlinePlayerMap.get(playerName).add(x);
                        else offlinePlayerMap.get(playerName).remove(x);
                        if (offlinePlayerMap.get(playerName).size() == 0) offlinePlayerMap.remove(playerName);
                    }
                }
        if (online) {
            if (!onlinePlayerMap.containsKey(playerName)) onlinePlayerMap.put(playerName, new HashSet<>());
            if (b) onlinePlayerMap.get(playerName).add(interactionName);
            else onlinePlayerMap.get(playerName).remove(interactionName);
            if (onlinePlayerMap.get(playerName).size() == 0) onlinePlayerMap.remove(playerName);
        } else {
            if (!offlinePlayerMap.containsKey(playerName)) offlinePlayerMap.put(playerName, new HashSet<>());
            if (b) offlinePlayerMap.get(playerName).add(interactionName);
            else offlinePlayerMap.get(playerName).remove(interactionName);
            if (offlinePlayerMap.get(playerName).size() == 0) offlinePlayerMap.remove(playerName);
        }
    }

    public static void onPlayerConnect(PlayerEntity player) {
        String playerName = player.getName().getString();
        if (offlinePlayerMap.containsKey(playerName)) {
            for (String x : offlinePlayerMap.get(playerName)) {
                setPlayerInteraction(playerName, x, true, true);
            }
            offlinePlayerMap.remove(playerName);
        }
    }

    public static void onPlayerDisconnect(PlayerEntity player) {
        String playerName = player.getName().getString();
        if (onlinePlayerMap.containsKey(playerName)) {
            for (String x : onlinePlayerMap.get(playerName)) {
                setPlayerInteraction(playerName, x, true, false);
            }
            onlinePlayerMap.remove(playerName);
        }
    }

    protected static ServerPlayerEntity playerFromName(String name) {
        return CarpetServer.minecraft_server.getPlayerManager().getPlayer(name);
    }
}