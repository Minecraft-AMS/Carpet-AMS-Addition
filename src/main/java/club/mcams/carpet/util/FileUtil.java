package club.mcams.carpet.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
