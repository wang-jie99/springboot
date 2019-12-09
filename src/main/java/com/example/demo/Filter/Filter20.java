package com.example.demo.Filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Order(2)
@WebFilter(filterName = "Filter 20", urlPatterns = {"/*"})
public class Filter20 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        //强制类型转换为HTTPServlet
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //得到请求的资源的路径
        String path = request.getRequestURI();
        //打印被请求的资源名称和请求时间
        System.out.println(path + " @ " + new Date());
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //判断path中是否包含"/login",若包含，则不验证访问权限
        if (!path.contains("/login.ctl")) {
            //访问权限验证
            HttpSession session = request.getSession(false);
            System.out.println("Session:  " + session);
            if (session == null || session.getAttribute("currentUser") == null) {
                message.put("message", "请登录或重新登录");
                //响应到前端
                response.getWriter().println(message);
                return;
            }
        }
        //执行其他过滤器，如过滤器执行完毕，则执行原请求
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }
    @Override
    public void destroy() { }
}