package schoolwork.bookstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.util.Build;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class addBooks {

    @Autowired
    BookMapper bookMapper;

    @Test
    void addBook() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("books.json")), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> root = mapper.readValue(json, Map.class);
        Map<String, Object> data = (Map<String, Object>) root.get("data");
        List<Map<String, Object>> result = (List<Map<String, Object>>) data.get("result");
        for (Map<String, Object> book : result) {
            bookMapper.addBookByJson(
                    Build.buildBid(),
                    book.get("title").toString(),
                    book.get("author").toString(),
                    book.get("publish").toString()
            );
        }

        System.out.println("全部 JSON 数据已成功写入数据库！");


    }
}
