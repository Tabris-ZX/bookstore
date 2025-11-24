package zx.bookstore.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 购物车
 */
@Data
@TableName("carts")
public class Cart {
    private String id;
    private Integer uid;
    private Integer bid;
    private Integer number;
}
