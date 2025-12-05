package schoolwork.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import schoolwork.bookstore.dto.AIMemory;
import schoolwork.bookstore.model.ChatHistory;

import java.util.List;

@Mapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {

    @Select("SELECT question, answer FROM chat_history " +
            "WHERE uid = #{uid} AND created_time >= NOW() - INTERVAL 3 DAY " +
            "ORDER BY created_time LIMIT 20")
    List<AIMemory> getHistoryForAI(@Param("uid") long uid);
}
