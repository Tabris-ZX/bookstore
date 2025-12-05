package schoolwork.bookstore.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AddData {
    public void addBookByJson() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("data.json")), "UTF-8");

    }
}
