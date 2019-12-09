package com.example.demo.bysj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.School;
import com.example.demo.bysj.service.SchoolService;
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

/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
@RestController
public class SchoolController{
    /**
     * POST, http://49.235.12.142:8080/bysj1837/school.ctl, 增加学院
     * 增加一个学院对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request 请求对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/school.ctl", method = RequestMethod.POST)
    public JSONObject add(HttpServletRequest request) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //前台没有为id赋值，此处模拟自动生成id。如果Dao能真正完成数据库操作，删除下一行。
        schoolToAdd.setId(4 + (int)(Math.random()*100));
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加School对象
        try {
            boolean result = SchoolService.getInstance().add(schoolToAdd);
            if (result) {
                message.put("message", "增加成功");
            }else {
                message.put("message", "增加失败");
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();

        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }

    /**
     * DELETE, http://49.235.12.142:8080/bysj1837/school.ctl?id=1, 删除id=1的学院
     * 删除一个学院对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/school.ctl", method = RequestMethod.DELETE)
    protected JSONObject delete(@RequestParam(value = "id",required = false) String id_str){
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            SchoolService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }


    /**
     * PUT, http://49.235.12.142:8080/bysj1837/school.ctl, 修改学院
     *
     * 修改一个学院对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/school.ctl", method = RequestMethod.PUT)
    protected JSONObject update(HttpServletRequest request) throws ServletException, IOException {
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToUpdate = JSON.parseObject(school_json, School.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改School对象对应的记录
        try {
            boolean result = SchoolService.getInstance().update(schoolToUpdate);
            if (result) {
                message.put("message", "修改成功");
            }else {
                message.put("message", "修改失败");
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }

    /**
     * GET, http://49.235.12.142:8080/bysj1837/school.ctl?id=1, 查询id=1的学院
     * GET, http://49.235.12.142:8080/bysj1837/school.ctl, 查询所有的学院
     * 把一个或所有学院对象响应到前端
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/school.ctl", method = RequestMethod.GET)
    public String list(@RequestParam(value = "id", required = false) String id_str){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                return responseSchools();
            } else {
                int id = Integer.parseInt(id_str);
                return responseSchool(id);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        }
    }

    //响应一个学院对象
    private String responseSchool(int id) throws IOException, SQLException {
        //根据id查找学院
        School school = SchoolService.getInstance().find(id);
        String school_json = JSON.toJSONString(school);
        //返回school_json
        return school_json;
    }
    //响应所有学院对象
    private String responseSchools() throws IOException, SQLException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools, SerializerFeature.DisableCircularReferenceDetect);
        //返回schools_json
        return schools_json;
    }
}
