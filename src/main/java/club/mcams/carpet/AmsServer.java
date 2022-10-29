
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import club.mcams.carpet.command.amscarpetCommandRegistry;
import club.mcams.carpet.function.ChunkLoading;
import club.mcams.carpet.logging.amscarpetLoggerRegistry;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class AmsServer implements CarpetExtension, ModInitializer {
    @Override
    public void registerLoggers() {
        amscarpetLoggerRegistry.registerLoggers();
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        amscarpetCommandRegistry.register(dispatcher);
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player) {
        ChunkLoading.onPlayerConnect(player);
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player) {
        ChunkLoading.onPlayerDisconnect(player);
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
