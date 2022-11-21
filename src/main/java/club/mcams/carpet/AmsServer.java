
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import club.mcams.carpet.command.AmsCarpetCommandRegistry;
import club.mcams.carpet.function.ChunkLoading;
import club.mcams.carpet.logging.AmsCarpetLoggerRegistry;
import club.mcams.carpet.util.AmsCarpetTranslations;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AmsServer implements CarpetExtension, ModInitializer {
    @Override
    public void registerLoggers() {
        AmsCarpetLoggerRegistry.registerLoggers();
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        AmsCarpetCommandRegistry.register(dispatcher);
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
    public void onInitialize() {
        AmsServer.loadExtension();
    }

    @Override
    public void onGameStarted() {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(AmsServerSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return AmsCarpetTranslations.getTranslationFromResourcePath(lang);
    }
}
