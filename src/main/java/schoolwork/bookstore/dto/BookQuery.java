package schoolwork.bookstore.dto;

import lombok.Data;

@Data
public class BookQuery {
    private String keyword;
    private String author;
    private String tags;
    private boolean stock;
}
