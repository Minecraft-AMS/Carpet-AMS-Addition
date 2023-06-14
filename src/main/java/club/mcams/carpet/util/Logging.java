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

package club.mcams.carpet.util;

import club.mcams.carpet.AmsServer;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class Logging {
    public static void logStackTrace(Throwable e) {
        Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
        dejaVu.add(e);

        // Print our stack trace
        AmsServer.LOGGER.error(e);
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            AmsServer.LOGGER.error("\tat " + traceElement);
        }

        // Print suppressed exceptions, if any
        for (Throwable se : e.getSuppressed()) {
            logEnclosedStackTrace(se, trace, "Suppressed: ", "\t", dejaVu);
        }

        // Print cause, if any
        Throwable ourCause = e.getCause();
        if (ourCause != null) {
            logEnclosedStackTrace(ourCause, trace, "Caused by: ", "", dejaVu);
        }
    }

    private static void logEnclosedStackTrace(
            Throwable e,
            StackTraceElement[] enclosingTrace,
            String caption,
            String prefix,
            Set<Throwable> dejaVu
    ) {
        if (dejaVu.contains(e)) {
            AmsServer.LOGGER.error(prefix + caption + "[CIRCULAR REFERENCE: " + e + "]");
        } else {
            dejaVu.add(e);
            // Compute number of frames in common between e and enclosing trace
            StackTraceElement[] trace = e.getStackTrace();
            int m = trace.length - 1;
            int n = enclosingTrace.length - 1;
            while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
                m--;
                n--;
            }
            int framesInCommon = trace.length - 1 - m;

            // Print our stack trace
            AmsServer.LOGGER.error(prefix + caption + e);
            for (int i = 0; i <= m; i++) {
                AmsServer.LOGGER.error(prefix + "\tat " + trace[i]);
            }
            if (framesInCommon != 0) {
                AmsServer.LOGGER.error(prefix + "\t... " + framesInCommon + " more");
            }

            // Print suppressed exceptions, if any
            for (Throwable se : e.getSuppressed()) {
                logEnclosedStackTrace(se, trace, "Suppressed: ", prefix + "\t", dejaVu);
            }

            // Print cause, if any
            Throwable ourCause = e.getCause();
            if (ourCause != null) {
                logEnclosedStackTrace(ourCause, trace, "Caused by: ", prefix, dejaVu);
            }
        }
    }
}
