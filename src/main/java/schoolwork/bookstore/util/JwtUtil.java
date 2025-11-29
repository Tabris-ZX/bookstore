package schoolwork.bookstore.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import schoolwork.bookstore.config.JwtConfig;
import schoolwork.bookstore.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    static JwtConfig jwtConfig;
    public JwtUtil(JwtConfig jwtConfig){
        JwtUtil.jwtConfig = jwtConfig;
    }

    public static String getJwt(User user){

        Map<String,Object> map = new HashMap<>();
        map.put("uid",user.getUid());
        map.put("username",user.getUsername());
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .compact();
        return jwt;
    }

    public static Claims parseJwt(String jwt){
        return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(jwt).getBody();
    }

    public static long getUid(HttpServletRequest request){
        String jwt = request.getAttribute("uid").toString();
        return Long.parseLong(jwt);
    }

}
