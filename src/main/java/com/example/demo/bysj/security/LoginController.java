package com.example.demo.bysj.security;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.bysj.domain.User;
import com.example.demo.bysj.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login.ctl")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JSONObject message = new JSONObject();
        try {
            User loggerUser = UserService.getInstance().login(username, password);
            if (loggerUser != null) {
                message.put("message", "Login successful");
                HttpSession session = request.getSession();
                //10分钟没有操作，则session失效
                session.setMaxInactiveInterval(10 * 60);
                session.setAttribute("currentUser", loggerUser);
                response.getWriter().println(message);
                //重定向到索引页（菜单页）
                return;
            } else {
                message.put("message", "用户名或密码错误");
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
    }
}
