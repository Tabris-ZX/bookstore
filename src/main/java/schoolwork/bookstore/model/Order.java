package schoolwork.bookstore.model;

import ch.qos.logback.core.joran.sanity.Pair;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 订单实体类
 */
@Data
@NoArgsConstructor
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

    public Order(long uid,long bid, int number) {
        this.oid= UUID.randomUUID().toString();
        this.uid = uid;
        this.items = Map.of(bid,number);
    }
    public Order(long uid, Map<Long,Integer> books,double price) {
        this.oid= UUID.randomUUID().toString();
        this.uid = uid;
        this.items = books;
        this.totalPrice = price;
    }
}




