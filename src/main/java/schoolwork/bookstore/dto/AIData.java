package schoolwork.bookstore.dto;


import lombok.Data;

@Data
public class AIData {
    private String title;
    private String author;
    private String tags;
    private String description;
    private Integer rating;
    private Integer stock;
}
