package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.devices.*;
import com.team.boeboard.form.devices.brightness.Function5Dto;
import com.team.boeboard.form.devices.screenshot.*;
import com.team.boeboard.form.devices.timing.Function2Dto;
import com.team.boeboard.form.devices.volume.Function4Dto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DevicesService {
    /**
     * 添加设备
     *
     * @param deviceAddInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    DevicesDto add(DevicesAddInputDto deviceAddInputDto) throws BoeBoardServiceException;

    /**
     * 控制——屏幕截图（vue发送指令给android，android收到后截屏）
     *
     * @return
     * @throws BoeBoardServiceException
     */
    VueReturnDto screenshot1() throws BoeBoardServiceException;

    /**
     * 控制——屏幕截图（接受android传来的图片）
     *
     * @param photo
     * @return
     * @throws BoeBoardServiceException
     */
    ScreenshotUploadReturnDto screenshot2(MultipartFile photo) throws BoeBoardServiceException;

    /**
     * 控制——屏幕截图（vue调用接口把截屏信息发给vue）
     *
     * @return
     * @throws BoeBoardServiceException
     */
    ScreenshotGetDto screenshot3() throws BoeBoardServiceException;

    /**
     * 控制——自动开关机（vue发送指令给android，android收到后执行自动开关机）
     *
     * @param function2Dto
     * @return
     * @throws BoeBoardServiceException
     */
    VueReturnDto timing(Function2Dto function2Dto) throws BoeBoardServiceException;

    /**
     * 控制——重启系统（vue发送指令给android，android收到后执行重启系统）
     *
     * @return
     * @throws BoeBoardServiceException
     */
    VueReturnDto restart() throws BoeBoardServiceException;

    /**
     * 控制——音量控制（vue发送指令给android，android收到后执行音量控制）
     *
     * @param function4Dto
     * @return
     * @throws BoeBoardServiceException
     */
    VueReturnDto volume(Function4Dto function4Dto) throws BoeBoardServiceException;

    /**
     * 控制——亮度控制（vue发送指令给android，android收到后执行亮度控制）
     *
     * @param function5Dto
     * @return
     * @throws BoeBoardServiceException
     */
    VueReturnDto brightness(Function5Dto function5Dto) throws BoeBoardServiceException;

    /**
     * 控制——系统升级（vue发送指令给android，android收到后执行系统升级）
     *
     * @return
     * @throws BoeBoardServiceException
     */
    VueReturnDto upgrade() throws BoeBoardServiceException;

    /**
     * 控制——清除缓存（vue发送指令给android，android收到后执行清除缓存）
     *
     * @return
     * @throws BoeBoardServiceException
     */
    VueReturnDto clearcache() throws BoeBoardServiceException;

    /**
     * 编辑设备
     *
     * @param deviceEditInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    DevicesDto edit(DevicesEditInputDto deviceEditInputDto) throws BoeBoardServiceException;

    /**
     * 获取所有设备
     *
     * @return
     * @throws BoeBoardServiceException
     */
    List<DevicesAllDto> getAll() throws BoeBoardServiceException;

    /**
     * 设备上下线状态
     *
     * @param dstatus
     * @return
     * @throws BoeBoardServiceException
     */
    String upAndDown(String dstatus) throws BoeBoardServiceException;


    /**
     * 设备更新
     *
     * @throws BoeBoardServiceException
     */
    ApkInfoDto GetApkInfo() throws BoeBoardServiceException;
}
