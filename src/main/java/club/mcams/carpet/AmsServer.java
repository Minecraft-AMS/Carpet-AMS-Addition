
package club.mcams.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmsServer implements CarpetExtension, ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("TMCLoggerMod");

    private static int indent = 0;
    private static String indentString = "";

    public static void increaseIndent() {
        indent++;
        indentString = " ".repeat(indent * 4);
    }

    public static void decreaseIndent() {
        if (indent >= 0) {
            indent--;
            indentString = " ".repeat(indent * 4);
        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void debug(String str) {
        LOGGER.debug(indentString + str);
    }

    public static void error(String str) {
        LOGGER.error(indentString + str);
    }

    public static void info(String str) {
        LOGGER.info(indentString + str);
    }

    public static void warn(String str) {
        LOGGER.warn(indentString + str);
    }

    public static void globalInfo(String str) {
        info(str);
        Util.broadcastToAllPlayers(str);
    }

    @Override
    public String version() {
        return "carpet-extra";
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
