package com.example.demo.bysj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.User;
import com.example.demo.bysj.service.UserService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class UserController {
        /**
         * PUT, http://49.235.12.142:8080/bysj1837/user.ctl, 修改学位
         * <p>
         * 修改一个学位对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
         *
         * @param request
         * @throws ServletException
         * @throws IOException
         */
        @RequestMapping(value = "/user.ctl", method = RequestMethod.PUT)
        protected JSONObject update(HttpServletRequest request) throws IOException {
            String user_json = JSONUtil.getJSON(request);
            //将JSON字串解析为user对象
            User user = JSON.parseObject(user_json, User.class);
            //创建JSON对象message，以便往前端响应信息
            JSONObject message = new JSONObject();
            //到数据库表修改Degree对象对应的记录
            try {
                boolean update = UserService.getInstance().updateUser(user);
                if(update) {
                    message.put("message", "修改成功");
                }else{
                    message.put("message", "未修改成功");
                }
            } catch (SQLException e) {
                message.put("message", "数据库操作异常");
                e.printStackTrace();
            } catch (Exception e) {
                message.put("message", "网络异常");
                e.printStackTrace();
            }
            //响应message到前端
            return message;
        }

        /**
         * GET, http://49.235.12.142:8080/bysj1837/degree.ctl?id=1, 查询id=1的学位
         * GET, http://49.235.12.142:8080/bysj1837/degree.ctl, 查询所有的学位
         * 把一个或所有学位对象响应到前端
         *
         * @throws ServletException
         * @throws IOException
         */
        @RequestMapping(value = "/uers.ctl", method = RequestMethod.GET)
        protected String list(@RequestParam(value = "id",required = false) String id_str,
                            @RequestParam(value = "username", required = false) String username){
            //创建JSON对象message，以便往前端响应信息
            JSONObject message = new JSONObject();
            try {
                //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
                if (id_str == null && username == null) {
                    return responseUsers();
                } else if (id_str == null) {
                    return responseUserByUsername(username);
                }else {
                    int id = Integer.parseInt(id_str);
                    return responseUser(id);
                }
            } catch (SQLException e) {
                message.put("message", "数据库操作异常");
                e.printStackTrace();
                //响应message到前端
                return message.toString();
            } catch (Exception e) {
                message.put("message", "网络异常");
                e.printStackTrace();
                //响应message到前端
                return message.toString();
            }
        }

        //响应一个学位对象
        private String responseUser(int id) throws SQLException {
            //根据id查找学位
            User user = UserService.getInstance().find(id);
            String user_json = JSON.toJSONString(user);
            //响应degree_json到前端
            return user_json;
        }

    private String responseUserByUsername(String name) throws SQLException {
        //指定schoolId的所有专业对象
        User user = UserService.getInstance().findByUsername(name);
        String userByUsername_json = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
        //响应departments_json到前端
        return userByUsername_json;
    }

    //响应所有学位对象
    private String responseUsers() throws SQLException {
        //获得所有学位
        Collection<User> users = UserService.getInstance().findAll();
        String users_json = JSON.toJSONString(users, SerializerFeature.DisableCircularReferenceDetect);
        //响应degrees_json到前端
        return users_json;
    }
}
