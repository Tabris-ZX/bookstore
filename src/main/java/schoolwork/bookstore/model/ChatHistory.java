package schoolwork.bookstore.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@TableName("chat_history")
public class ChatHistory {
    @TableId
    private String id;
    private Long uid;
    private String question;
    private String answer;
    @TableField("created_time")
    private LocalDateTime createdTime;

    public ChatHistory(long uid,String question, String answer) {
        this.id = UUID.randomUUID().toString();
        this.uid = uid;
        this.question = question;
        this.answer = answer;
    }
}
