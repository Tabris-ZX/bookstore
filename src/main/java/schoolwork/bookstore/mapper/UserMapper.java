package schoolwork.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import schoolwork.bookstore.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE uid = #{uid}")
    User getUserByUid(@Param("uid") long uid);

    @Update("UPDATE users set status = 0 where uid = #{uid}")
    boolean banUser(@Param("uid") long uid);

    @Update("UPDATE users SET status = 1 WHERE uid = #{uid}")
    boolean unbanUser(@Param("uid") long uid);

    @Update("UPDATE users SET username = #{username} WHERE uid = #{uid}")
    void updateUsername(@Param("uid") long uid, @Param("username") String username);

    @Update("UPDATE users SET password = #{password} WHERE uid = #{uid}")
    void updatePassword(@Param("uid") long uid, @Param("password") String password);

}
