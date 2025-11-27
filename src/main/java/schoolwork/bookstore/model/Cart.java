package schoolwork.bookstore.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 购物车
 */
@Data
@NoArgsConstructor
@TableName("carts")
public class Cart {
    private String id;
    private Long uid;
    private Long bid;
    private Integer number;

    public Cart(long uid, long bid, int number) {
        this.uid = uid;
        this.bid = bid;
        this.number = number;
    }
}
