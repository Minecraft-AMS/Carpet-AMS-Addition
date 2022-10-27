package club.mcams.carpet.command;

import carpet.utils.Messenger;

import club.mcams.carpet.function.Interactions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

import com.mojang.brigadier.arguments.BoolArgumentType;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.command.CommandSource.suggestMatching;

public class InteractionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("interaction").
                executes((c) -> listPlayerInteractions(c.getSource(), c.getSource().getName())).
                then(argument("interaction", StringArgumentType.word()).
                        suggests((c, b) -> suggestMatching(Interactions.interactionList, b)).
                        then(argument("boolean", BoolArgumentType.bool()).
                                executes((c) -> setPlayerInteraction(c.getSource(), c.getSource().getName(), getString(c, "interaction"), getBool(c, "boolean")))
                        )));

    }

    private static int setPlayerInteraction(ServerCommandSource source, String playerName, String interactionName, boolean b) {
        PlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        if (player == null) {
            Messenger.m(source, "r No player specified");
            return 0;
        }
        if (!Interactions.interactionList.contains(interactionName)) {
            Messenger.m(source, "r Unknown interaction: ", "rb " + interactionName);
            return 0;
        }
        Interactions.setPlayerInteraction(playerName, interactionName, !b, true);
        Messenger.m(source, "w Set interaction ", "g " + interactionName, "w  to ", "g " + b);
        return 1;
    }

    private static int listPlayerInteractions(ServerCommandSource source, String playerName) {
        PlayerEntity player = source.getServer().getPlayerManager().getPlayer(playerName);
        if (player == null) {
            Messenger.m(source, "r No player specified");
            return 0;
        }
        Set<String> playerInteractions = Interactions.onlinePlayerMap.getOrDefault(playerName, new HashSet<>());
        for (String x : Interactions.interactionList)
            if (!x.equals("all")) {
                if (playerInteractions.contains(x)) Messenger.m(source, "w " + x + ": ", "g false");
                else Messenger.m(source, "w " + x + ": ", "g true");
            }
        return 1;
    }
}