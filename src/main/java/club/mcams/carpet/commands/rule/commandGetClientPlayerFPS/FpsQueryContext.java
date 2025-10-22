package club.mcams.carpet.commands.rule.commandGetClientPlayerFPS;

import net.minecraft.server.command.ServerCommandSource;

public class FpsQueryContext {
    private final ServerCommandSource source;
    private final String playerName;

    protected FpsQueryContext(ServerCommandSource source, String playerName) {
        this.source = source;
        this.playerName = playerName;
    }

    protected ServerCommandSource getSource() {
        return source;
    }

    protected String getPlayerName() {
        return playerName;
    }
}
