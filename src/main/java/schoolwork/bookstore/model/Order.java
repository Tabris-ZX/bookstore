package schoolwork.bookstore.model;

import ch.qos.logback.core.joran.sanity.Pair;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 订单实体类
 */
@Data
@TableName("orders")
public class Order {
    @TableId
    private String oid;
    private Long uid;
    private Map<Long,Integer> items; //<bid,number>
    private Integer status;
    @TableField("total_price")
    private Double totalPrice;
    @TableField("created_time")
    private LocalDateTime createdTime;

    public  Order() {
    }
    public Order(long uid,long bid, int number) {
        this.uid = uid;
        this.items = Map.of(bid,number);
    }
}




