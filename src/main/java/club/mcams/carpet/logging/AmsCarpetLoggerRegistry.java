/*
 * This file is part of the Carpet AMS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  A Minecraft Server and contributors
 *
 * Carpet AMS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet AMS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet AMS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

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
