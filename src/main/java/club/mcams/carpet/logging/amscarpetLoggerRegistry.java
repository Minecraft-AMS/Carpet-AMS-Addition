package club.mcams.carpet.logging;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;

public class amscarpetLoggerRegistry {
    public static boolean __dragonPortalLocation;

    public static void registerLoggers() {
        LoggerRegistry.registerLogger("dragonPortalLocation", standardLogger("dragonPortalLocation", null, null));
    }

    static Logger standardLogger(String logName, String def, String[] options) {
        try {
            return new Logger(amscarpetLoggerRegistry.class.getField("__" + logName), logName, def, options);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to create logger " + logName);
        }
    }
}
