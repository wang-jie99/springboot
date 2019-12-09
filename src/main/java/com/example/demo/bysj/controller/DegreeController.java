package com.example.demo.bysj.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.Degree;
import com.example.demo.bysj.service.DegreeService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/degree.ctl")
public class DegreeController {
    //请使用以下JSON测试增加功能（id为空）
    //{"description":"id为null的新学位","no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的学位","id":36,"no":"05","remarks":""}

    /**myapp
     * POST, http://49.235.12.142:8080/bysj1837/degree.ctl, 增加学位
     * 增加一个学位对象：将来自前端请求的JSON对象，增加到数据库表中
     *
     * @param request  请求对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl", method = RequestMethod.POST)
    protected JSONObject add(HttpServletRequest request)
            throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Degree对象
        try {
            boolean add = DegreeService.getInstance().add(degreeToAdd);
            if (add) {
                message.put("message", "增加成功");
            }else {
                message.put("message", "增加失败");
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
     * DELETE, http://49.235.12.142:8080/bysj1837/degree.ctl?id=1, 删除id=1的学位
     * 删除一个学位对象：根据来自前端请求的id，删除数据库表中id的对应记录
     *
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl", method = RequestMethod.DELETE)
    protected JSONObject delete(@RequestParam(value = "id", required = false) String id_str){
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学位
        try {
           boolean result = DegreeService.getInstance().delete(id);
           if (result) {
               message.put("message", "删除成功");
           }else {
               message.put("message", "删除失败");
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
     * PUT, http://49.235.12.142:8080/bysj1837/degree.ctl, 修改学位
     * <p>
     * 修改一个学位对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     *
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl", method = RequestMethod.PUT)
    protected JSONObject update(HttpServletRequest request)
            throws ServletException, IOException {
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToUpdate = JSON.parseObject(degree_json, Degree.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Degree对象对应的记录
        try {
            boolean update = DegreeService.getInstance().update(degreeToUpdate);
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
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/degree.ctl", method = RequestMethod.GET)
    protected String list(@RequestParam(value = "id", required = false) String id_str) {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                return responseDegrees();
            } else {
                int id = Integer.parseInt(id_str);
                return responseDegree(id);
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
    private String responseDegree(int id) throws SQLException {
        //根据id查找学位
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        //响应degree_json到前端
        return degree_json;
    }

    //响应所有学位对象
    private String responseDegrees() throws SQLException {
        //获得所有学位
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees, SerializerFeature.DisableCircularReferenceDetect);
        //响应degrees_json到前端
        return degrees_json;
    }
}
