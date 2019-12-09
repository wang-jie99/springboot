package com.example.demo.bysj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.Department;
import com.example.demo.bysj.service.DepartmentService;
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
public class DepartmentController {
    /**
     * POST, http://49.235.12.142:8080/bysj1837/department.ctl, 增加专业
     * 增加一个专业对象：将来自前端请求的JSON对象，增加到数据库表中
     *
     * @param request  请求对象
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl", method = RequestMethod.POST)
    protected JSONObject add(HttpServletRequest request) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Department对象
        try {
            boolean add = DepartmentService.getInstance().add(departmentToAdd);
            if (add) {

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
     * DELETE, http://49.235.12.142:8080/bysj1837/department.ctl?id=1, 删除id=1的专业
     * 删除一个专业对象：根据来自前端请求的id，删除数据库表中id的对应记录
     *
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl", method = RequestMethod.DELETE)
    protected JSONObject delete(@RequestParam(value = "id", required = false) String id_str){
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的专业
        try {
            boolean delete = DepartmentService.getInstance().delete(id);
            if (delete) {
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
     * PUT, http://49.235.12.142:8080/bysj1837/department.ctl, 修改专业
     * <p>
     * 修改一个专业对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     *
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl", method = RequestMethod.PUT)
    protected JSONObject update(HttpServletRequest request) throws IOException {
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Department对象对应的记录
        try {
            boolean update = DepartmentService.getInstance().update(departmentToAdd);
            if (update) {
                message.put("message", "修改成功");
            }else {
                message.put("message", "修改失败");
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
     * GET, http://49.235.12.142:8080/bysj1837/department.ctl?id=1, 查询id=1的专业
     * GET, http://49.235.12.142:8080/bysj1837/department.ctl, 查询所有的专业
     * 把一个或所有专业对象响应到前端
     *
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/department.ctl", method = RequestMethod.GET)
    protected String list(@RequestParam(value = "id",required = false) String id_str,
                        @RequestParam(value = "pareType", required = false) String paraType){
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有专业对象，否则响应id指定的专业对象
            if (id_str == null) {
                return responseDepartments();
           } else {
                int id = Integer.parseInt(id_str);
                if (paraType == null) {
                    return responseDepartment(id);
                }else {
                    return responseDepartmentBySchoolId(id);
                }
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

    //响应一个专业对象
    private String responseDepartment(int id) throws SQLException {
        //根据id查找专业
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        //响应department_json到前端
        return department_json;
    }

    //响应指定schoolId的专业对象
    private String responseDepartmentBySchoolId(int schoolId) throws SQLException {
        //指定schoolId的所有专业对象
        Collection<Department> departmentsBySchool = DepartmentService.getInstance().findAllBySchool(schoolId);
        String departmentByScholl_json = JSON.toJSONString(departmentsBySchool, SerializerFeature.DisableCircularReferenceDetect);
        //响应departments_json到前端
        return departmentByScholl_json;
    }

    //响应所有专业对象
    private String responseDepartments() throws SQLException {
        //获得所有专业
        Collection<Department> departments = DepartmentService.getInstance().getAll();
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应departments_json到前端
        return departments_json;
    }
}
