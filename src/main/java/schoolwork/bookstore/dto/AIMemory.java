package schoolwork.bookstore.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AIMemory {
    private String question;
    private String answer;
}
