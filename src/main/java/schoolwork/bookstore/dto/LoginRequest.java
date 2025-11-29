package schoolwork.bookstore.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String loginType;
    private Long uid;
    private  String username;
    private Long phone;
    private String password;
}
