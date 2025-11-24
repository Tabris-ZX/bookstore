package zx.bookstore.model;

import ch.qos.logback.core.joran.sanity.Pair;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 订单实体类
 */
@Data
@TableName("orders")
public class Order {
    @TableId
    private String oid;
    private Integer uid;
    private List<Pair<Book,Integer>> items;
    private String status;
    @TableField("total_price")
    private Double totalPrice;
    @TableField("created_time")
    private LocalDateTime createdTime;
}




