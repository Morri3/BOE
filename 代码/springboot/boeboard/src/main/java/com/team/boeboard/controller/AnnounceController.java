package com.team.boeboard.controller;

import com.team.boeboard.form.announce.BackAnnounceDto;
import com.team.boeboard.form.announce.GetAnnounceDto;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/announce")
public class AnnounceController {

    @Autowired
    private AnnounceService announceService;

    /**
     * 新增一个公告
     * web端返回的数据结构
     *      {
     *          "content":"Helloword",      //公告内容
     *          "text_color":"#FFC0CB",     //字体颜色
     *          "text_size":12,             //字体大小
     *          "background_color":"#000000"//背景颜色
     *          "start_time":"2022-06-09 20:13:24"  //开始时间
     *          "finish_time":"2022-06-10 20:13:24" //结束时间
     *      }
     * @param dto
     * @param user_token    //用户token
     */
    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseData AddAnnounce(@RequestBody GetAnnounceDto dto,@RequestParam String user_token) throws ParseException {

        announceService.AddAnnounce(dto,user_token);

        return new ResponseData(ExceptionMsg.SUCCESS,"添加成功");
    }

    //删除公告
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseData DeleteAnnoune(@RequestBody Map map) {
        String content = (String) map.get("content");

        announceService.DeleteAnnounce(content);

        return new ResponseData(ExceptionMsg.SUCCESS, "删除成功");
    }

    /**
     * 获取所有公告
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getall",method = RequestMethod.GET)
    public ResponseData GetAllAnnounce(){
        List<BackAnnounceDto> dtos = announceService.GetAllAnnounces();

        return new ResponseData(ExceptionMsg.SUCCESS,dtos);
    }

}
