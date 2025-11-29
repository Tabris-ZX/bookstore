package schoolwork.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private Integer curPage;
    private Integer pageSize;
    //总页数
    private Integer totalPage;
    //总条目数
    private Long totalCount;
    private List<T> pageData;

    public PageResponse(Integer curPage, Integer pageSize, Long totalCount, List<T> pageData) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = (int) ((totalCount + pageSize - 1) / pageSize);
        this.pageData = pageData;
    }
}
