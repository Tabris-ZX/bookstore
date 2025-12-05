package schoolwork.bookstore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yitter.idgen.YitIdHelper;
import schoolwork.bookstore.dto.AIData;
import schoolwork.bookstore.dto.AIMemory;
import schoolwork.bookstore.dto.CartBooks;

import java.util.List;
import java.util.Map;

public class Build {

    static final ObjectMapper mapper = new ObjectMapper();

    public static long buildUid(){
        return 10_000_000+YitIdHelper.nextId()%10_000_000;
    }
//
    public static long buildBid(){
        return 100_000 + YitIdHelper.nextId()%100_000;
    }

    public static String buildJsonStr(List<CartBooks> map){
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static String buildMessageStr(List<AIMemory> chat){
        try {
            return mapper.writeValueAsString(chat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
