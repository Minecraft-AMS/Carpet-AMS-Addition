package club.mcams.carpet.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

@SuppressWarnings("unused")
public final class CommandHelper {
    private CommandHelper() {}
    /**
     * Notifies all players that the commands changed by resending the command tree.
     */
    public static void notifyPlayersCommandsChanged(MinecraftServer server) {
        if (server == null || server.getPlayerManager() == null) {
            return;
        }
        server.send(new ServerTask(server.getTicks(), () -> {
            try {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    server.getCommandManager().sendCommandTree(player);
                }
            }
            catch (NullPointerException ignored) {}
        }));
    }

    /**
     * Whether the given source has enough permission level to run a command that requires the given commandLevel
     */
    public static boolean canUseCommand(ServerCommandSource source, Object commandLevel) {
        if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
        String commandLevelString = commandLevel.toString();
        switch (commandLevelString) {
            case "true": return true;
            case "false": return false;
            case "ops": return source.hasPermissionLevel(2); // typical for other cheaty commands
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                return source.hasPermissionLevel(Integer.parseInt(commandLevelString));
        }
        return false;
    }
}