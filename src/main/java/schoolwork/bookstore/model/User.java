package schoolwork.bookstore.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import schoolwork.bookstore.util.Build;


/**
 * 用户实体类
 */
@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long uid;
    private String username;
    private String password;
    private Integer status;

    public User(String username, String password) {
        this.uid = Build.buildUid();
        this.username = username;
        this.password = password;
        this.status = 1;
    }
}
