package jerme.url.shortener.services;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileCreator {

    public static void write(String shorten) {
        Path path = Path.of("C:/Users/Jerryme/Documents/shortened_links");

        try {
            Files.createDirectories(path);

            var pathFile =  path.resolve("shortened_urls.txt");

            try (var fw = new FileWriter(pathFile.toFile(), true)) {
                fw.write("localhost:8080/" + shorten + "\n");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
