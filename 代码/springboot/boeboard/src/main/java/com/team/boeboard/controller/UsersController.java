package com.team.boeboard.controller;

import com.team.boeboard.form.users.*;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);//日志类

    @Autowired
    private UsersService usersService;

    //TODO 添加普通用户
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData register(@RequestBody RegisterDto registerDto) {
        UsersDto res = usersService.register(registerDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 用户登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(@RequestBody LoginDto loginDto) {
        String res = usersService.login(loginDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 获取用户信息
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getUserInfo(@RequestParam String token) {
        UsersEditDto res = usersService.getUserInfo(token);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 编辑用户信息
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData edit(@RequestBody UsersEditDto usersEditDto) {
        UsersEditDto res = usersService.edit(usersEditDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 启用
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData enable(@RequestBody UsersInfoDto usersInfoDto) {
        UsersStatusDto res = usersService.enable(usersInfoDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 停用
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData disable(@RequestBody UsersInfoDto usersInfoDto) {
        UsersStatusDto res = usersService.disable(usersInfoDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 删除用户
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(@RequestBody UsersInfoDto usersInfoDto) {
        UsersStatusDto res = usersService.delete(usersInfoDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 查询用户
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData search(@RequestBody UsersSearchDto usersSearchDto) {
        List<UsersSearchResultDto> res = usersService.search(usersSearchDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 获取用户列表
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getAll() {
        List<UsersSearchResultDto> res = usersService.getAll();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 用户登出
    @RequestMapping(value = "/logout/{token}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData logout(@PathVariable("token") String token) {
        String res = usersService.logout(token);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }
}
