package com.example.demo.Filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(filterName = "Filter 10", urlPatterns = {"/*"})//通配符*表示对所有资源有效
public class Filter10_Encoding implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        //强制类型转换为HTTPServlet
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //得到请求的资源的路径
        String path = request.getRequestURI();
        System.out.println("PATH:"+path);
        //判断path中是否包含"/login",若包含，则不设置字符编码
        if (!path.contains("/login")) {
            //设置响应字符编码为UTF-8
            response.setContentType("text/html;charset=UTF-8");
            //获得请求方法
            String method = request.getMethod();
            //如果方法时PUT或者POST
            if ("POST-PUT".contains(method)) {
                //设置请求字符编码为UTF-8
                request.setCharacterEncoding("UTF-8");
            }
        }
        //执行其他过滤器，如过滤器执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }
}