package schoolwork.bookstore.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


/**
 * 购物车
 */
@Data
@NoArgsConstructor
@TableName("carts")
public class Cart {
    private Long uid;
    private Long bid;
    private Integer number;
    @TableField("total_price")
    private Double totalPrice;

    public Cart(long uid, long bid, int number,double totalPrice) {
        this.uid = uid;
        this.bid = bid;
        this.number = number;
        this.totalPrice = totalPrice;
    }
}
