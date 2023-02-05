package com.team.boeboard.controller;

import com.team.boeboard.form.home.HomeInformDto;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    //TODO 获取首页信息
    @ResponseBody
    @RequestMapping(value = "/getinfo", method = RequestMethod.GET)
    public ResponseData GetHomeInfo() {
        HomeInformDto dto = homeService.GetHomeInfo();
        return new ResponseData(ExceptionMsg.SUCCESS, dto);
    }
}
