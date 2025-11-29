package schoolwork.bookstore.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import schoolwork.bookstore.model.Cart;

import java.util.Map;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    @Update("UPDATE carts SET number = #{number} WHERE uid = #{uid} AND bid = #{bid}")
    int updateBooksNumberInCart(@Param("uid") long uid,
                                @Param("bid") long bid,
                                @Param("number") int number);

    @Delete("DELETE from carts WHERE uid = #{uid}")
    boolean clearCart(@Param("uid") long uid);

    @Delete("DELETE from carts WHERE uid = #{uid} AND bid = #{bid}")
    boolean removeBooksInCart(@Param("uid") long uid,
                              @Param("bid") long bid);

    @Select("SELECT cart.bid, cart.number FROM carts cart WHERE cart.uid = #{uid}")
    Map<Long,Integer> getCartBooks(@Param("uid") long uid);

    @Select("SELECT SUM(price * number) FROM carts join books on carts.bid = books.bid WHERE uid = #{uid}")
    double getCartTotalPrice(long uid);

    @Insert("INSERT INTO carts(uid, bid, number) VALUES(#{uid}, #{bid}, #{number})" +
            "ON DUPLICATE KEY UPDATE number = #{number};\n")
    boolean insertOrUpdateBooksInCart(@Param("uid") long uid,
                                      @Param("bid") long bid,
                                      @Param("number") int number);
}
