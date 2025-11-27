package schoolwork.bookstore.pojo;

import lombok.Data;
import schoolwork.bookstore.model.Book;

import java.util.List;

@Data
public class PageResponse<T> {
    private Integer curPage;
    private Integer pageSize;
    //总页数
    private Integer totalPage;
    //总条目数
    private Long totalCount;
    private List<T> data;

    public PageResponse(Integer curPage, Integer pageSize, Long totalCount, List<T> data) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;
        this.totalPage = (int) ((totalCount + pageSize - 1) / pageSize);
    }
}
