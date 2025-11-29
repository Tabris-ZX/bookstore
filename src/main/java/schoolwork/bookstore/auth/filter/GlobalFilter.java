package schoolwork.bookstore.auth.filter;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import schoolwork.bookstore.dto.Result;
import schoolwork.bookstore.util.JwtUtil;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/books/*")
public class GlobalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String jwt = request.getHeader("token");
        try{
            JwtUtil.parseJwt(jwt);
        }catch (Exception e){
            response.setContentType("application/json;charset=UTF-8");
            Result result = Result.error("身份验证失败，请登录");
            response.getWriter().write(JSONObject.toJSONString(result));
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
