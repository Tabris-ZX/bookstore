package schoolwork.bookstore.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String type;
    private Long uid;
    private  String username;
    private Long phone;
    private String password;
}
