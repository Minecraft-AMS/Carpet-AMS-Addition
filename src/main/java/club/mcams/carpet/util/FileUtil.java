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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

//TODO: 暂时先不管这里，能用就行
@SuppressWarnings("unused")
public class FileUtil {
    public static String readFile(String path) throws IOException {
        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IOException("Null input stream from path " + path);
        }
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    public static void writeToFile(Path path, String text) throws IOException {
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(text);
        }
    }

    public static String getFileExtension(String path) {
        return FilenameUtils.getExtension(path);
    }

    public static String removeFileExtension(String path) {
        return FilenameUtils.removeExtension(path);
    }
}
