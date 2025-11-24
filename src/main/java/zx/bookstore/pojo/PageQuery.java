package zx.bookstore.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageQuery {
    private Integer pageNum;
    private Integer pageSize = 10;
    private List<Object> data;
}
