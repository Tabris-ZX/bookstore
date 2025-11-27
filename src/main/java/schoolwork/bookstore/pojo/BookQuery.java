package schoolwork.bookstore.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BookQuery {
    //private Long bid;
    private int curPage=0;
    private int size=0;
    private String author;
    private String tags;
    private double price;
    private boolean stock;
}
