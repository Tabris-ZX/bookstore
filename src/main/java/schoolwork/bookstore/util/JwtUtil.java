package schoolwork.bookstore.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import schoolwork.bookstore.config.JwtConfig;
import schoolwork.bookstore.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    JwtConfig jwtConfig;
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String getJwt(User user){

        Map<String,Object> map = new HashMap<>();
        map.put("uid",user.getUid());
        map.put("username",user.getUsername());
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .compact();
        return jwt;
    }

    public Claims parseJwt(String jwt){
        return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(jwt).getBody();
    }
}
