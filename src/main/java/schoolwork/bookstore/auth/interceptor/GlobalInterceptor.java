package schoolwork.bookstore.auth.interceptor;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import schoolwork.bookstore.dto.Result;
import schoolwork.bookstore.util.JwtUtil;

@Component
public class GlobalInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String jwt = request.getHeader("token");

        if (jwt == null || jwt.isEmpty()) {
            return writeError(response, "缺少 token，请登录");
        }
        try {
            Claims claims = JwtUtil.parseJwt(jwt);
            Long uid = claims.get("uid", Long.class);
            if (uid == null) {
                return writeError(response, "token 无效，请重新登录");
            }
            request.setAttribute("uid", uid);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return writeError(response, "登录已过期，请重新登录");
        } catch (io.jsonwebtoken.SignatureException e) {
            return writeError(response, "token 已被篡改，请重新登录");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            return writeError(response, "无效的 token，请重新登录");
        } catch (Exception e) {
            return writeError(response, "身份验证失败，请重新登录");
        }
    }

    private boolean writeError(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        Result result = Result.error(msg);
        response.getWriter().write(JSONObject.toJSONString(result));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
