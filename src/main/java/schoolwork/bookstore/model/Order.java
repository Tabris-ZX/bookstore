package schoolwork.bookstore.model;

import ch.qos.logback.core.joran.sanity.Pair;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import schoolwork.bookstore.dto.CartBooks;
import schoolwork.bookstore.util.Build;

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
    private String books; //<bid,number>
    private String name;
    private Long phone;
    private String address;
    private Integer status;
    @TableField("total_price")
    private Double totalPrice;
    @TableField("created_time")
    private LocalDateTime createdTime;

    public Order(long uid,long bid, int number, double price,UserAddr addr) {
        this.oid= UUID.randomUUID().toString();
        this.uid = uid;
        this.books = "{\""+bid+"\":"+number+"}";
        this.totalPrice = price;
        if (addr != null) {
            this.name = addr.getName();
            this.phone = addr.getPhone();
            this.address = addr.getAddress();
        } else {
            this.name = null;
            this.phone = null;
            this.address = null;
        }
//
//        this.name = addr.getName();
//        this.phone = addr.getPhone();
//        this.address = addr.getAddress();
    }
    public Order(long uid, List<CartBooks> books, double price, UserAddr addr) {
        this.oid= UUID.randomUUID().toString();
        this.uid = uid;
        this.books = Build.buildJsonStr(books);
        this.totalPrice = price;
        if (addr != null) {
            this.name = addr.getName();
            this.phone = addr.getPhone();
            this.address = addr.getAddress();
        } else {
            this.name = null;
            this.phone = null;
            this.address = null;
        }
    }
}
