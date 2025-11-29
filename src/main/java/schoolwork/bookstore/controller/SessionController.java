package schoolwork.bookstore.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import schoolwork.bookstore.dto.Result;

@RestController
public class SessionController {

    @GetMapping("/setCookie")
    public Result setCookie(HttpServletResponse response){
        response.addCookie(new Cookie("sessionId", "123456789"));
        return Result.success();
    }

    @GetMapping("/getCookie")
    public Result getCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String sessionId = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("sessionId")){
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        if(sessionId != null){
            return Result.success("Session ID: " + sessionId);
        } else {
            return Result.error("No session ID found");
        }
    }
}
