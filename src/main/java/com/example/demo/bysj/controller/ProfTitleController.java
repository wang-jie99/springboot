package com.example.demo.bysj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.bysj.domain.ProfTitle;
import com.example.demo.bysj.service.ProfTitleService;
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
public class ProfTitleController {
    /**
     * POST, http://49.235.12.142:8080/bysj1837/profTitle.ctl, 增加课题
     * 增加一个课题对象：将来自前端请求的JSON对象，增加到数据库表中
     *
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.POST)
    protected JSONObject add(HttpServletRequest request)throws IOException{
        //根据request对象，获得代表参数的JSON字串
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加ProfTitle对象
        try {
            boolean add = ProfTitleService.getInstance().add(profTitleToAdd);
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
     * DELETE, http://49.235.12.142:8080/bysj1837/profTitle.ctl?id=1, 删除id=1的课题
     * 删除一个课题对象：根据来自前端请求的id，删除数据库表中id的对应记录
     *
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.DELETE)
    protected JSONObject delete(@RequestParam(value = "id", required = false) String id_str) {
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的课题
        try {
            boolean delete = ProfTitleService.getInstance().delete(id);
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
     * PUT, http://49.235.12.142:8080/bysj1837/profTitle.ctl, 修改课题
     * <p>
     * 修改一个课题对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     *
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.PUT)
    protected JSONObject update(HttpServletRequest request) throws ServletException, IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToUpdate = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改ProfTitle对象对应的记录
        try {
            boolean update = ProfTitleService.getInstance().update(profTitleToUpdate);
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
     * GET, http://49.235.12.142:8080/bysj1837/profTitle.ctl?id=1, 查询id=1的课题
     * GET, http://49.235.12.142:8080/bysj1837/profTitle.ctl, 查询所有的课题
     * 把一个或所有课题对象响应到前端
     *
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.GET)
    protected String list(@RequestParam(value = "id", required = false) String id_str) {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有课题对象，否则响应id指定的课题对象
            if (id_str == null) {
                return responseProfTitles();
            } else {
                int id = Integer.parseInt(id_str);
                return responseProfTitle(id);
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

    //响应一个课题对象
    private String responseProfTitle(int id) throws  SQLException {
        //根据id查找课题
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);
        //响应profTitle_json到前端
        return profTitle_json;
    }

    //响应所有课题对象
    private String responseProfTitles() throws SQLException {
        //获得所有课题
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().getAll();
        String profTitles_json = JSON.toJSONString(profTitles, SerializerFeature.DisableCircularReferenceDetect);
        //响应profTitles_json到前端
        return profTitles_json;
    }
}
