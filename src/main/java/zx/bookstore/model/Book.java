package zx.bookstore.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 图书实体类
 */
@Data
@TableName("books")
public class Book {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long bid;
    private String title;
    private String author;
    private String description;
    @TableField("cover_url")
    private String coverUrl;
    private String[] tags;
    private Double price;
    private Integer stock;
}
