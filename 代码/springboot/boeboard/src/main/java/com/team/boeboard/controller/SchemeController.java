package com.team.boeboard.controller;

import com.team.boeboard.form.scheme.*;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.SchemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheme")
public class SchemeController {
    private final Logger logger = LoggerFactory.getLogger(DevicesController.class);//日志类

    @Autowired
    private SchemeService schemeService;

    //TODO 添加计划
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData add(@RequestParam String token, @RequestBody SchemeAddInputDto schemeAddInputDto) {
        SchemeDto res = schemeService.add(token, schemeAddInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 获取所有计划
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getAll() {
        List<SchemeAllInfoDto> res = schemeService.getAll();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 计划详情
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData info(@RequestBody SchemeInfoAndDeleteInputDto schemeInfoAndDeleteInputDto) {
        SchemeInfoDto res = schemeService.info(schemeInfoAndDeleteInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 编辑计划
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData edit(@RequestBody SchemeEditInputDto schemeEditInputDto) {
        SchemeDto res = schemeService.edit(schemeEditInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 删除计划
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody SchemeInfoAndDeleteInputDto schemeInfoAndDeleteInputDto) {
        SchemeDeleteDto res = schemeService.delete(schemeInfoAndDeleteInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 查询计划
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData search(@RequestBody SchemeSearchInputDto schemeSearchInputDto) {
        List<SchemeDto> res = schemeService.search(schemeSearchInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 复制计划
    @RequestMapping(value = "/reproduce/{token}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData reproduce(@PathVariable("token") String token,
                                  @RequestBody SchemeReproduceInputDto schemeReproduceInputDto) {
        long res = schemeService.reproduce(token, schemeReproduceInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 重新发布计划
    @RequestMapping(value = "/republish", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData republish(@RequestBody SchemeRepublishInputDto schemeRepublishInputDto) {
        long res = schemeService.republish(schemeRepublishInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

}
