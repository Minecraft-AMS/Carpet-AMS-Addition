package club.mcams.carpet.command;

import carpet.utils.Messenger;
import club.mcams.carpet.AmsServerSettings;
import club.mcams.carpet.function.ChunkLoading;
import club.mcams.carpet.util.CommandHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class amscarpetCommandRegistry {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("chunkloading")
                .requires((player) -> CommandHelper.canUseCommand(player, AmsServerSettings.commandChunkLoading))
                .executes((c) -> listPlayerInteractions(c.getSource(), c.getSource().getName()))
                .then(argument("boolean", BoolArgumentType.bool()).
                        executes((c) -> setPlayerInteraction(c.getSource(), c.getSource().getName(), getBool(c, "boolean")))
                ));

    }

    private static int setPlayerInteraction(ServerCommandSource source, String playerName, boolean b) {
        PlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        if (player == null) {
            Messenger.m(source, "r No player specified");
            return 0;
        }
        ChunkLoading.setPlayerInteraction(playerName, b, true);
        Messenger.m(source, "w Set interaction ", "g " + "chunk loading", "w  to ", "g " + b);
        return 1;
    }

    private static int listPlayerInteractions(ServerCommandSource source, String playerName) {
        PlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        if (player == null) {
            Messenger.m(source, "r No player specified");
            return 0;
        }
        boolean playerInteractions = ChunkLoading.onlinePlayerMap.getOrDefault(playerName, true);

        if (playerInteractions) Messenger.m(source, "w " + "chunk loading" + ": ", "g true");
        else Messenger.m(source, "w " + "chunk loading" + ": ", "g false");
        return 1;
    }
}