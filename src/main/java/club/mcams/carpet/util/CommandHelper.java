package club.mcams.carpet.util;

import net.minecraft.command.CommandSource;

/**
 * From gnembon's carpet mod lol
 * <p>
 * A few helpful methods to work with settings and commands.
 * <p>
 * This is not any kind of API, but it's unlikely to change
 */
public final class CommandHelper {
    private CommandHelper() {
    }

    /**
     * Whether the given source has enough permission level to run a command that requires the given commandLevel
     */
    public static boolean canUseCommand(CommandSource source, Object commandLevel) {
        if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
        String commandLevelString = commandLevel.toString();
        return switch (commandLevelString) {
            case "true" -> true;
            case "false" -> false;
            case "ops" -> source.hasPermissionLevel(2); // typical for other cheaty commands
            case "0", "1", "2", "3", "4" -> source.hasPermissionLevel(Integer.parseInt(commandLevelString));
            default -> false;
        };
    }
}
