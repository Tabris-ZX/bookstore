package schoolwork.bookstore.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import schoolwork.bookstore.config.AIConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
@Component
public class AIUtil {
    private final AIConfig.Gemini gemini;
    private final AIConfig.SiliconFlow siliconFlow;
    private final AIConfig.Ark ark;

    public AIUtil(AIConfig.Gemini gemini, AIConfig.SiliconFlow siliconFlow,AIConfig.Ark ark) {
        this.gemini = gemini;
        this.siliconFlow = siliconFlow;
        this.ark = ark;
    }
    String basePrompt = """
            你是一个图书推荐专家,阅书无数.以上这些数据是这家图书商店的所有图书信息和你与用户最近的聊天记录\
            你要根据这些图书信息和聊天记录给**浏览商城的用户**推荐适合的书籍或者建议\
            **语言要精炼,分析要切中要点,不要偏离主题,回复一定要准确!**\
            回答仅根据这些图书,可以搜索网络上这些图书的信息,但一定不要虚构图书信息!!!
            
            下面是用户的需求,解决他的问题:
            
            """;

    @Async
    public CompletableFuture<String> chat(String wanting, String bookInfo, String chatInfo) {
        String prompt = "商城图书信息:\n"+bookInfo+"\n与用户聊天记录:\n"+chatInfo+basePrompt+wanting;

        return getArkRecommendation(prompt);
//        return getSiliconFlowRecommendation(wanting, bookInfo);
//        return getGeminiRecommendation(wanting, bookInfo);
    }

    public void chatStream(String wanting, String bookInfo, String chatInfo,Consumer<String> onChunk){
        String prompt = "商城图书信息:\n"+bookInfo+"\n与用户聊天记录:\n"+chatInfo+basePrompt+wanting;
        getArkRecommendationStream(prompt, onChunk);
    }

    @Async
    public CompletableFuture<String> getGeminiRecommendation(String prompt){
        JSONObject requestBody = new JSONObject();
        requestBody.put("contents", new JSONObject[]{
                new JSONObject() {{
                    put("parts",new JSONObject[]{
                            new JSONObject() {{
                                put("text", prompt);
                            }}
                    });
                }}
        });
        requestBody.put("generationConfig", new JSONObject() {{
            put("thinkingConfig", new JSONObject(){{
                put("thinkingBudget", 0);
            }});
        }});
        String resp = sendRequest(requestBody.toJSONString(),gemini);
        return CompletableFuture.completedFuture(resp);
    }

    @Async
    public CompletableFuture<String> getSiliconFlowRecommendation(String prompt){
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", siliconFlow.getModel());
        requestBody.put("enable_thinking", false);
        requestBody.put("messages", new Object[] {
                new JSONObject() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        String resp = sendRequest(requestBody.toJSONString(),siliconFlow);
        return CompletableFuture.completedFuture(resp);
    }

    @Async
    public CompletableFuture<String> getArkRecommendation(String prompt){
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", ark.getModel());
        requestBody.put("stream", false);
        requestBody.put("input", new Object[] {
                new JSONObject() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        String resp = sendRequest(requestBody.toJSONString(),ark);
        return CompletableFuture.completedFuture(resp);
    }

    /**
     * 流式获取AI推荐
     * @param prompt 提示词
     * @param onChunk 处理每个数据块的回调函数
     */
    public void getArkRecommendationStream(String prompt, Consumer<String> onChunk) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", ark.getModel());
        requestBody.put("stream", true);
        requestBody.put("input", new Object[] {
                new JSONObject() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        sendStreamRequest(requestBody.toJSONString(), ark, onChunk);
    }

    private String sendRequest(String requestBody, AIConfig aiConfig) {
        int tries = 3;
        while (tries-->0) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(aiConfig.getApiBaseUrl()))
                        .header(aiConfig.getHeader(), aiConfig.getApiKey())
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(15))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpClient client = HttpClient.newBuilder()
                        .proxy(ProxySelector.of(new InetSocketAddress("127.0.0.1", 7890)))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return getAIResponse(response,aiConfig.getType());

            } catch (Exception e) {
                if (tries==0) return "尝试三次后错误:" + e.getMessage();
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
        }
        return "Unexpected error.";
    }

    private String getAIResponse(HttpResponse<String> response,String type) {
        try{
            switch (type) {
                case "gemini" -> {
                    return JSONObject.parseObject(response.body())
                            .getJSONArray("candidates").getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts").getJSONObject(0)
                            .getString("text");
                }
                case "ark" -> {
                    return JSONObject.parseObject(response.body())
                            .getJSONArray("output").getJSONObject(0)
                            .getJSONArray("content").getJSONObject(0)
                            .getString("text");
                }default -> {
                    return response.body();
                }
            }
        }catch (Exception e){
            log.error("返回文本解析失败");
            return response.body();
        }
    }

    /**
     * 发送流式请求并处理响应
     */
    private void sendStreamRequest(String requestBody, AIConfig aiConfig, Consumer<String> onChunk) {
        int tries = 3;
        while (tries-- > 0) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(aiConfig.getApiBaseUrl()))
                        .header(aiConfig.getHeader(), aiConfig.getApiKey())
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(60))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpClient client = HttpClient.newBuilder()
                        .proxy(ProxySelector.of(new InetSocketAddress("127.0.0.1", 7890)))
                        .build();

                HttpResponse<InputStream> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofInputStream()
                );

                if (response.statusCode() != 200) {
                    log.error("流式请求失败，状态码: {}", response.statusCode());
                    onChunk.accept("错误: HTTP状态码 " + response.statusCode());
                    return;
                }

                processStreamResponse(response.body(), aiConfig.getType(), onChunk);
                return;

            } catch (Exception e) {
                log.error("流式请求失败: {}", e.getMessage(), e);
                if (tries == 0) {
                    onChunk.accept("错误: " + e.getMessage());
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    /**
     * 处理流式响应，解析SSE格式并提取内容
     */
    private void processStreamResponse(InputStream inputStream, String type, Consumer<String> onChunk) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            boolean isDone = false;
            
            while ((line = reader.readLine()) != null && !isDone) {
                line = line.trim();

                if (line.isEmpty()||line.startsWith("event: ")) continue;
                
                if (line.startsWith("data: ")) {
                    String jsonStr = line.substring(6).trim();
                    
                    if ("[DONE]".equals(jsonStr)) {
                        isDone = true;
                        break;
                    }

                    try {
                        JSONObject json = JSONObject.parseObject(jsonStr);
                        
                        if ("ark".equals(type)) {
                            String eventType = json.getString("type");
                            
                            if ("response.done".equals(eventType)) {
                                isDone = true;
                                break;
                            }
                            
                            // 处理所有delta事件（包括output_text.delta和其他delta事件）
                            if (eventType != null && eventType.contains("delta")) {
                                String content = extractContentFromDelta(json);
                                if (content != null && !content.isEmpty()) {
                                    onChunk.accept(content);
                                }
                            }
                            // 处理content_part.added事件
                            else if ("response.content_part.added".equals(eventType)) {
                                String content = extractContentFromPart(json);
                                if (content != null && !content.isEmpty()) {
                                    onChunk.accept(content);
                                }
                            }
                            // 兼容OpenAI格式
                            else if (json.containsKey("choices")) {
                                JSONObject choice = json.getJSONArray("choices").getJSONObject(0);
                                if (choice.containsKey("delta")) {
                                    JSONObject delta = choice.getJSONObject("delta");
                                    if (delta.containsKey("content")) {
                                        String content = delta.getString("content");
                                        if (content != null && !content.isEmpty()) {
                                            onChunk.accept(content);
                                        }
                                    }
                                }
                                if (choice.containsKey("finish_reason") && "stop".equals(choice.getString("finish_reason"))) {
                                    isDone = true;
                                    break;
                                }
                            }
                        } else {
                            if (json.containsKey("content")) {
                                String content = json.getString("content");
                                if (content != null && !content.isEmpty()) {
                                    onChunk.accept(content);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // 忽略无法解析的行
                    }
                } else {
                    try {
                        JSONObject json = JSONObject.parseObject(line);
                        if (json.containsKey("choices")) {
                            JSONObject choice = json.getJSONArray("choices").getJSONObject(0);
                            if (choice.containsKey("delta")) {
                                JSONObject delta = choice.getJSONObject("delta");
                                if (delta.containsKey("content")) {
                                    String content = delta.getString("content");
                                    if (content != null && !content.isEmpty()) {
                                        onChunk.accept(content);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // 忽略无法解析的行
                    }
                }
            }
        } catch (Exception e) {
            log.error("处理流式响应失败: {}", e.getMessage(), e);
            onChunk.accept("错误: 处理流式响应失败 - " + e.getMessage());
        }
    }

    private String extractContentFromDelta(JSONObject json) {
        Object delta = json.get("delta");
        if (delta instanceof String) {
            return (String) delta;
        }
        JSONObject deltaObj = (JSONObject) delta;
        return deltaObj.getString("text") != null ? deltaObj.getString("text") : deltaObj.getString("content");
    }

    private String extractContentFromPart(JSONObject json) {
        JSONObject part = json.getJSONObject("part");
        if (part != null) {
            return part.getString("text");
        }
        JSONObject item = json.getJSONObject("item");
        if (item != null) {
            return item.getString("text");
        }
        return json.getString("text");
    }
}
