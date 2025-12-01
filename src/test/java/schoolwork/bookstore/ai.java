package schoolwork.bookstore;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import schoolwork.bookstore.config.AIConfig;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.model.Book;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import java.util.List;

@SpringBootTest
public class ai {

    @Autowired
//    AIConfig.SiliconFlow config;
    AIConfig.Gemini config;

    @Autowired
    BookMapper bookMapper;


    @Test
    public void testGemini() throws Exception {
        List<Book> books = bookMapper.getAllBooksInfo();
        String bookInfo = JSON.toJSONString(books);
        // 用户想要的内容
        String wanting = "我很喜欢金庸,给我推荐几本他的武侠小说吧!";
        String prompt = "你是一个图书推荐专家,以下是图书商店的所有图书信息,根据这些图书信息推荐一本适合的书籍:\n" + bookInfo;

        JSONObject requestBody = new JSONObject();
        requestBody.put("contents", new JSONObject[]{
                new JSONObject() {{
                        put("parts",new JSONObject[]{
                                new JSONObject() {{
                                    put("text", wanting + "\n" + prompt);
                                }}
                        });
                }}
        });
        requestBody.put("generationConfig", new JSONObject() {{
            put("thinkingConfig", new JSONObject(){{
                put("thinkingBudget", 0);
            }});
        }});

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(config.getApiBaseUrl())) // 例如 https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent
                .header("x-goog-api-key", config.getApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    @Test
    public void testAI() throws Exception {
        List<Book> books = bookMapper.getAllBooksInfo(); // 从数据库拿数据
        String bookInfo = JSON.toJSONString(books);
        String wanting ="我很喜欢金庸,给我推荐几本他的类似的武侠小说吧!";
        String prompt = "你是一个图书推荐专家,以下是图书商店的所有图书信息,根据这些图书信息推荐一本适合的书籍.\n"+bookInfo;
        HttpClient client = HttpClient.newHttpClient();
        JSONObject json = new JSONObject();
        json.put("model", config.getModel());
        json.put("enable_thinking", false);
        json.put("messages", new Object[] {
                new JSONObject() {{
                    put("role", "user");
                    put("content", wanting+"\n"+prompt);
                }}
        });
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(config.getApiBaseUrl()))
                .header("Authorization", "Bearer " + config.getApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

}
