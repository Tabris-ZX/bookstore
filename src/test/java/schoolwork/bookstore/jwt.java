package schoolwork.bookstore;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class jwt {

    @Test
    public void test(){

        Map<String,Object> map = new HashMap<>();
        map.put("uid",1);
        map.put("username","zx");
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "zhangxu")
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .compact();
        System.out.println(jwt);
    }

    @Test
    public void parse(){
        String jwt="eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjEsImV4cCI6MTc2NDA1Mzg5MywidXNlcm5hbWUiOiJ6eCJ9.89sOb3JRDGmrK2RQ33_qtBaZzsY6DTLRYlItodBTEog";
        Claims parse = Jwts.parser().setSigningKey("zhangxu").parseClaimsJws(jwt).getBody();
        System.out.println(parse);
    }
}
