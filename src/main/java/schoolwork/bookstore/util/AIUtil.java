package schoolwork.bookstore.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import schoolwork.bookstore.config.AIConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Component
public class AIUtil {
    private final AIConfig.Gemini gemini;
    private final AIConfig.SiliconFlow siliconFlow;
    public AIUtil(AIConfig.Gemini gemini, AIConfig.SiliconFlow siliconFlow) {
        this.gemini = gemini;
        this.siliconFlow = siliconFlow;

    }
    String prompt = """
            你是一个图书推荐专家,阅书无数.以上这些数据是这家图书商店的所有图书信息,\
            你要根据这些图书信息给用户推荐适合的书籍或者建议,语言要精炼,分析要切中要点,不要偏离主题\
            
            下面是用户的需求:
            
            """;

    @Async
    public CompletableFuture<String> getGeminiRecommendation(String wanting, String bookInfo){
        JSONObject requestBody = new JSONObject();
        requestBody.put("contents", new JSONObject[]{
                new JSONObject() {{
                    put("parts",new JSONObject[]{
                            new JSONObject() {{
                                put("text", bookInfo+prompt+wanting);
                            }}
                    });
                }}
        });
        requestBody.put("generationConfig", new JSONObject() {{
            put("thinkingConfig", new JSONObject(){{
                put("thinkingBudget", 0);
            }});
        }});
        String resp = sendRequest(requestBody.toJSONString(),"x-goog-api-key");
        return CompletableFuture.completedFuture(resp);
    }

    @Async
    public CompletableFuture<String> getSiliconFlowRecommendation(String wanting, String bookInfo){
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", gemini.getModel());
        requestBody.put("enable_thinking", false);
        requestBody.put("messages", new Object[] {
                new JSONObject() {{
                    put("role", "user");
                    put("content", bookInfo+prompt+wanting);
                }}
        });
        String resp = sendRequest(requestBody.toJSONString(),"Authorization");
        return CompletableFuture.completedFuture(resp);

    }

    private String sendRequest(String requestBody, String header) {
        int tries = 3,attempt = 0;
        while (attempt < tries) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(gemini.getApiBaseUrl()))
                        .header(header, gemini.getApiKey())
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(15))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return getAIResponse(response);

            } catch (Exception e) {
                attempt++;
                if (attempt >= tries) {
                    return "Error after " + tries + " attempts: " + e.getMessage();
                }
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }
        return "Unexpected error.";
    }

    private String getAIResponse(HttpResponse<String> response) {
        return JSONObject.parseObject(response.body())
                .getJSONArray("candidates").getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts").getJSONObject(0)
                .getString("text");
    }

}
