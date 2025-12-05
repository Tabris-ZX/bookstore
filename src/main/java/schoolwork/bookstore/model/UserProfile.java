package schoolwork.bookstore.model;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("user_profile")
public class UserProfile {

    @TableId
    private Long uid;
    private String email;
    private String prefer;

}
