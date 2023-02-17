
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import club.mcams.carpet.command.AmsCarpetCommandRegistry;
import club.mcams.carpet.function.ChunkLoading;
import club.mcams.carpet.logging.AmsCarpetLoggerRegistry;
import club.mcams.carpet.util.AmsCarpetTranslations;
import club.mcams.carpet.util.AutoMixinAuditExecutor;
import club.mcams.carpet.util.Logging;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
//#if MC>=11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.util.WorldSavePath;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class AmsServer implements CarpetExtension, ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("Ams");
//    private static MinecraftServer minecraftServer;

    @Override
    public void registerLoggers() {
        AmsCarpetLoggerRegistry.registerLoggers();
    }


    //#if MC>=11900
    //$$    @Override
    //$$    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess commandBuildContext) {
    //$$        AmsCarpetCommandRegistry.register(dispatcher);
    //$$    }
    //#else
    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        AmsCarpetCommandRegistry.register(dispatcher);
    }
    //#endif

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
    public void onInitialize() {
        AmsServer.loadExtension();
        AutoMixinAuditExecutor.run();
    }

    @Override
    public void onGameStarted() {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(AmsServerSettings.class);
        LOGGER.info("Carpet-AMS-Addition Loaded!");
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return AmsCarpetTranslations.getTranslationFromResourcePath(lang);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        File datapackPath = new File(server.getSavePath(WorldSavePath.DATAPACKS).toString() + "/AmsData/data/");
        if (Files.isDirectory(datapackPath.toPath())) {
            try {
                FileUtils.deleteDirectory(datapackPath);
            } catch (IOException e) {
                Logging.logStackTrace(e);
            }
        }
    }

//    @Override
//    public void onServerLoaded(MinecraftServer server) {
//        minecraftServer = server;
//    }
}

