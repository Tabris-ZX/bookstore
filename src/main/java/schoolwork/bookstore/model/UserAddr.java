package schoolwork.bookstore.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("user_info")
public class UserInfo {

    @TableId
    private Long uid;
    private String name;
    private String email;
    private Long phone;
    private String address;
    private String prefer;
    private Double balance;

    public UserInfo(long uid){
        this.uid = uid;
    }
}
