package schoolwork.bookstore.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_profiles")
public class UserProfile {

    @TableId
    private Long uid;
    private String name;
    private String email;
    private Long phone;
    private String address;
    private String prefer;

}
