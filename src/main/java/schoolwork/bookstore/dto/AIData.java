package schoolwork.bookstore.dto;


import lombok.Data;

@Data
public class AIData {
    private Long bid;
    private String title;
    private String author;
    private String tags;
    private String description;
    private Integer stock;
}
