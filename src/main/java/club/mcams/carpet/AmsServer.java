
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import club.mcams.carpet.command.InteractionCommand;
import club.mcams.carpet.function.Interactions;
import club.mcams.carpet.logging.amscarpetLoggerRegistry;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmsServer implements CarpetExtension, ModInitializer {

    @Override
    public void registerLoggers() {
        amscarpetLoggerRegistry.registerLoggers();
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        InteractionCommand.register(dispatcher);
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player) {
        Interactions.onPlayerConnect(player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player) {
        Interactions.onPlayerDisconnect(player);
    }

    @Override
    public String version() {
        return "carpet-ams-addition";
    }

    public static void loadExtension() {
        CarpetServer.manageExtension(new AmsServer());
    }

    @Override
    public void onInitialize()
    {
        AmsServer.loadExtension();
    }

    @Override
    public void onGameStarted()
    {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(AmsServerSettings.class);
    }
}
