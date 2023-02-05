package com.team.boeboard.controller;

import com.team.boeboard.form.groupings.GroupingsAddAndEditInputDto;
import com.team.boeboard.form.groupings.GroupingsDeleteInputDto;
import com.team.boeboard.form.groupings.GroupingsDto;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.GroupingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupings")
public class GroupingsController {
    private final Logger logger = LoggerFactory.getLogger(GroupingsController.class);//日志类

    @Autowired
    private GroupingsService groupingsService;

    //TODO 添加分组
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData add(@RequestBody GroupingsAddAndEditInputDto groupingsAddAndEditInputDto) {
        GroupingsDto res = groupingsService.add(groupingsAddAndEditInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 编辑分组
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData edit(@RequestBody GroupingsAddAndEditInputDto groupingsAddAndEditInputDto) {
        GroupingsDto res = groupingsService.edit(groupingsAddAndEditInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 删除分组
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody GroupingsDeleteInputDto groupingsDeleteInputDto) {
        GroupingsDto res = groupingsService.delete(groupingsDeleteInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 获取所有分组
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getAll() {
        List<GroupingsDto> res = groupingsService.getAll();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }
}