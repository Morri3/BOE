package com.team.boeboard.controller;

import com.team.boeboard.form.devices.*;
import com.team.boeboard.form.devices.brightness.Function5Dto;
import com.team.boeboard.form.devices.screenshot.*;
import com.team.boeboard.form.devices.timing.Function2Dto;
import com.team.boeboard.form.devices.volume.Function4Dto;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.DevicesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DevicesController {
    private final Logger logger = LoggerFactory.getLogger(DevicesController.class);//日志类

    @Autowired
    private DevicesService devicesService;

    //TODO 添加设备
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData add(@RequestBody DevicesAddInputDto devicesAddInputDto) {
        DevicesDto res = devicesService.add(devicesAddInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——屏幕截图（vue发送指令给android，android收到后截屏）
    @RequestMapping(value = "/control/screenshot", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData screenshot1() {
        VueReturnDto res = devicesService.screenshot1();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——屏幕截图（接受android传来的图片）
    @RequestMapping(value = "/control/screenshot/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData screenshot2(@RequestBody MultipartFile photo) {
        ScreenshotUploadReturnDto res = devicesService.screenshot2(photo);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——屏幕截图（vue调用接口把截屏信息发给vue）
    @RequestMapping(value = "/control/screenshot/givetovue", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData screenshot3() {
        ScreenshotGetDto res = devicesService.screenshot3();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——自动开关机（vue发送指令给android，android收到后执行自动开关机）
    @RequestMapping(value = "/control/timing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData timing1(@RequestBody Function2Dto function2Dto) {
        VueReturnDto res = devicesService.timing(function2Dto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——重启系统（vue发送指令给android，android收到后执行重启系统）
    @RequestMapping(value = "/control/restart", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData restart() {
        VueReturnDto res = devicesService.restart();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——音量控制（vue发送指令给android，android收到后执行音量控制）
    @RequestMapping(value = "/control/volume", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData volume(@RequestBody Function4Dto function4Dto) {
        VueReturnDto res = devicesService.volume(function4Dto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——亮度控制（vue发送指令给android，android收到后执行亮度控制）
    @RequestMapping(value = "/control/brightness", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData brightness(@RequestBody Function5Dto function5Dto) {
        VueReturnDto res = devicesService.brightness(function5Dto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——系统升级（vue发送指令给android，android收到后执行系统升级）
    @RequestMapping(value = "/control/upgrade", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData upgrade() {
        VueReturnDto res = devicesService.upgrade();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 控制——清除缓存（vue发送指令给android，android收到后执行清除缓存）
    @RequestMapping(value = "/control/clearcache", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData clearcache() {
        VueReturnDto res = devicesService.clearcache();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 编辑设备
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData edit(@RequestBody DevicesEditInputDto devicesEditInputDto) {
        DevicesDto res = devicesService.edit(devicesEditInputDto);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 获取所有设备
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getAll() {
        List<DevicesAllDto> res = devicesService.getAll();
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    //TODO 设备上下线状态
    @RequestMapping(value = "/updown", method = RequestMethod.POST)
    public ResponseData upAndDown(@RequestParam String dstatus) {
        String res = devicesService.upAndDown(dstatus);
        return new ResponseData(ExceptionMsg.SUCCESS, res);
    }

    /**
     * 获取更新后的apk和json
     * @return ApkInfoDto
     */
    @RequestMapping(value = "/getapk", method = RequestMethod.POST)
    public ResponseData updateDevice() {
        ApkInfoDto dto = devicesService.GetApkInfo();
        return new ResponseData(ExceptionMsg.SUCCESS, dto);
    }

}
