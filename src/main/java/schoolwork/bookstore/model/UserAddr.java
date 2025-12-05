package schoolwork.bookstore.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("user_addr")
public class UserAddr {

    @TableId
    private Long uid;
    private String name;
    private Long phone;
    private String address;

    public UserAddr(long uid){
        this.uid = uid;
    }
}
