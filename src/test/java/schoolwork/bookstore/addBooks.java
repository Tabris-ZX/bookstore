package schoolwork.bookstore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StreamUtils;
import schoolwork.bookstore.config.PathConfig;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.util.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @Autowired
    PathConfig pathConfig;

    @Test
    void addBook() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(pathConfig.getDataDir()+"/top250.json")), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> books = mapper.readValue(json, List.class);
        for (Map<String, Object> book : books) {
            bookMapper.addBookByJson(
                    Build.buildBid(),
                    book.get("isbn").toString(),
                    book.get("title").toString(),
                    book.get("author").toString(),
                    book.get("publisher").toString(),
                    book.get("comment").toString(),
                    book.get("cover_url").toString(),
                    Integer.parseInt(book.get("rating").toString()),
                    Double.parseDouble(book.get("price").toString()),
                    Integer.parseInt(book.get("rating_count").toString())
            );

        }

        System.out.println("全部 JSON 数据已成功写入数据库！");
    }
    @Test
    void addCover() throws IOException {
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");
        List<String> ISBN = bookMapper.getBooksISBN();
        String saveDir = pathConfig.getImageDir() + "/bookCovers/";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (String isbn : ISBN) {
            File coverFile = new File(saveDir, isbn + ".png");
            if (coverFile.exists()) {
                System.out.println("已存在封面，跳过: " + isbn);
                continue; // 文件已存在，跳过下载
            }
            try {
                String key = "9cd3763b9784415584c7781f736391f2";
                String apiUrl = "https://data.isbn.work/openApi/getInfoByIsbn?isbn=" + isbn + "&appKey=" + key;

                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    String response = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    inputStream.close();

                    // 解析 JSON
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response);
                    JsonNode data = root.path("data");
                    String bookDesc = data.path("bookDesc").asText();
                    bookMapper.changeDesc(isbn, bookDesc);
                    JsonNode picturesNode = data.path("pictures");

                    if (picturesNode.isTextual()) {
                        // 这里 pictures 是一个 JSON 字符串，需要再解析
                        String picturesStr = picturesNode.asText();
                        JsonNode picArray = mapper.readTree(picturesStr);

                        for (JsonNode picUrlNode : picArray) {
                            String picUrl = picUrlNode.asText();
                            System.out.println("下载封面: " + picUrl);

                            // 下载图片
                            URL pic = new URL(picUrl);
                            HttpURLConnection picConn = (HttpURLConnection) pic.openConnection();
                            picConn.setRequestMethod("GET");
                            picConn.setConnectTimeout(10000);
                            picConn.setReadTimeout(10000);

                            if (picConn.getResponseCode() == 200) {
                                File saveFile = new File(saveDir + isbn + ".png");
                                saveFile.getParentFile().mkdirs();

                                try (InputStream picStream = picConn.getInputStream();
                                     FileOutputStream fos = new FileOutputStream(saveFile)) {
                                    StreamUtils.copy(picStream, fos);
                                }
                                System.out.println("封面下载完成: " + isbn);
                            } else {
                                System.out.println("封面下载失败: " + isbn);
                            }
                            picConn.disconnect();
                        }
                    } else {
                        System.out.println("没有封面数据: " + isbn);
                    }
                } else {
                    System.out.println("请求失败: " + isbn);
                }
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("处理 ISBN " + isbn + " 时出错: " + e.getMessage());
            }
        }
    }

}
