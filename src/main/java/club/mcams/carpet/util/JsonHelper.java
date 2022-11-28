package club.mcams.carpet.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class JsonHelper {
    public static void writeJson(JsonObject jsonObject, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject));
            writer.close();
        } catch (IOException e) {
            Logging.logStackTrace(e);
        }
    }

    public static JsonObject readJson(String filePath) {
        try {
            FileReader reader = new FileReader(filePath);
            //#if MC>=11800
            return JsonParser.parseReader(reader).getAsJsonObject();
            //#else
            //$JsonParser parser = new JsonParser();
            //$return parser.parse(reader).getAsJsonObject();
            //#endif
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
