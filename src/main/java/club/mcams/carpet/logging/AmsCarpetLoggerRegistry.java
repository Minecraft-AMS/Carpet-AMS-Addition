package club.mcams.carpet.logging;

import carpet.logging.Logger;
public class AmsCarpetLoggerRegistry {

    public static void registerLoggers() {
//        LoggerRegistry.registerLogger("dragonPortalLocation", standardLogger("dragonPortalLocation", null, null));
    }

    static Logger standardLogger(String logName, String def, String[] options) {
        try {
            return new Logger(AmsCarpetLoggerRegistry.class.getField("__" + logName), logName, def, options
                    //#if MC>=11700
                    , true
                    //#endif
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to create logger " + logName);
        }
    }
}
