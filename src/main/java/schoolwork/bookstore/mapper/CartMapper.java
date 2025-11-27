package schoolwork.bookstore.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import schoolwork.bookstore.model.Cart;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    @Update("UPDATE carts SET number = #{number} WHERE uid = #{uid} AND bid = #{bid}")
    int updateBooksNumberInCart(@Param("uid") long uid,
                                @Param("bid") long bid,
                                @Param("number") int number);
}
