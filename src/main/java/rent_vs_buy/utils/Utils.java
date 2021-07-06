package rent_vs_buy.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static String getFileContent(String path) {
        Path resourceDirectory = Paths.get("src","test","resources");
        String pathPrefix = resourceDirectory.toFile().getAbsolutePath();
        String absolutePath = pathPrefix + path;
        try {
            return Files.readString(Paths.get(absolutePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
