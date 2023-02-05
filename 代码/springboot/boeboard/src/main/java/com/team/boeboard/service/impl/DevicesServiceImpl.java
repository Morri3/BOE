package com.team.boeboard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.team.boeboard.entity.AndroidApk;
import com.team.boeboard.entity.Devices;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.MaterialUploadDto;
import com.team.boeboard.form.devices.*;
import com.team.boeboard.form.devices.brightness.Function5Dto;
import com.team.boeboard.form.devices.screenshot.*;
import com.team.boeboard.form.devices.timing.Function2Dto;
import com.team.boeboard.form.devices.volume.Function4Dto;
import com.team.boeboard.form.log.CntLogDeviceDto;
import com.team.boeboard.form.log.CntLogDto;
import com.team.boeboard.form.log.SchemeAddLogDto;
import com.team.boeboard.minio.MinIOService;
import com.team.boeboard.mq.MqMessage;
import com.team.boeboard.repository.AndroidApkRepository;
import com.team.boeboard.repository.DevicesRepository;
import com.team.boeboard.service.DevicesService;
import com.team.boeboard.utils.Constants;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DevicesServiceImpl implements DevicesService {
    private final Logger logger = LoggerFactory.getLogger(DevicesServiceImpl.class);

    @Autowired
    private DevicesRepository devicesRepository;
    @Autowired
    private AndroidApkRepository apkRepository;
    @Autowired
    private AmqpTemplate mqService;//mq
    @Autowired
    private RedisTemplate redisTemplate;//redis缓存
    @Autowired
    private MinIOService minIOService;//MinIO

    public static int index = 0;//截屏的下标
    public static int index_log = 0;//事件日志下标
    public static int i = 0;//截屏第三个接口

    @Override
    public DevicesDto add(DevicesAddInputDto deviceAddInputDto) throws BoeBoardServiceException {
        DevicesDto res = new DevicesDto();//存放结果

        if (deviceAddInputDto != null) {
            //获取传入dto的信息
            String dname = deviceAddInputDto.getDname();
            String mac2 = deviceAddInputDto.getMac2();
            String inst = deviceAddInputDto.getInst();
            String sn = deviceAddInputDto.getSn();

            //判断数据库是否存在该设备（以mac地址为基准）
            Devices device = devicesRepository.findDeviceByMac(mac2);
            if (device != null) {//存在该设备
                //返回错误提示信息
                logger.warn("设备已存在，添加失败");
                res.setFailed(true);
                res.setId(0);
                res.setDname(dname);
                res.setMac2(mac2);
                res.setMemo("设备已存在，添加失败");
            } else {//数据库不存在该设备，可以添加
                //添加到数据库
                devicesRepository.addDevice(dname, inst, mac2, sn);

                //获取刚刚创建的设备的实体
                long d_id = devicesRepository.searchCreatedDevice();
                Devices dev = devicesRepository.searchDeviceByDid(d_id);//根据id查找实体

                //填充返回的dto
                res.setFailed(false);//没发生异常
                res.setId(1);
                res.setDname(dname);
                res.setInst(inst);
                res.setGroupings(dev.getGroupings());
                res.setMac2(mac2);
                res.setDpi(dev.getDpi());
                res.setDstatus(dev.getDstatus());
                res.setVer1(dev.getVer1());
                res.setMemo("设备添加成功");
            }
        }
        return res;
    }

    @Override
    public VueReturnDto screenshot1() throws BoeBoardServiceException {
        VueReturnDto res = new VueReturnDto();//存放结果

        //消息通过消息队列发给android
        MqMessage msg = new MqMessage(MqMessage.CATEGORY_SCREENSHOT);
        msg.appendContent("memo", "开始屏幕截图");//备注
//            mqService.convertAndSend(Constants.QUE_SCREENSHOT, msg.stringfy());//msg.stringfy()转json
        mqService.convertSendAndReceive(Constants.QUE_SCREENSHOT, msg.stringfy());//msg.stringfy()转json

        //填充返回给vue的dto
        res.setFailed(false);
        res.setId(1);
        res.setMemo("发送屏幕截图的消息成功");

        return res;
    }

    @Override
    public ScreenshotUploadReturnDto screenshot2(MultipartFile photo) throws BoeBoardServiceException {
        ScreenshotUploadReturnDto res = new ScreenshotUploadReturnDto();//存放结果

        if (photo != null) {
            //把图片存入minio，获得图片的信息
            MinioClient minioClient = minIOService.getMinioClient();
            if (minioClient == null) {
                throw new BoeBoardServiceException("连接失败");
            }
            MaterialUploadDto dto = minIOService.UploadScreenshot(minioClient, photo);
            if (dto != null) {
                //填充返回给android的dto
                res.setFailed(false);
                res.setId(1);
                res.setMurl(dto.getUrl());//url
                res.setName(dto.getFile_name());//截屏名称（非完整url）
                res.setMemo("截屏成功");

                //把res存入缓存，vue端调用接口c时获取该res的数据
                index++;
                redisTemplate.opsForValue().set(ScreenshotUploadReturnDto.cacheKey(index), JSONObject.toJSONString(res));
                logger.warn("存储截屏的信息[{}]", ScreenshotUploadReturnDto.cacheKey(index));
            }
        } else {
            logger.warn("截屏失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("截屏失败");
        }
        return res;
    }

    @Override
    public ScreenshotGetDto screenshot3() throws BoeBoardServiceException {
        ScreenshotGetDto res = new ScreenshotGetDto();//存放结果

//        //使用Timer，延迟1s后吧缓存中的截屏信息set到返回的res中
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("延迟1秒->开始取出缓存中的截屏信息");
//
//                try {
//                    if (i == 1) {//执行完一次就退出timer
//                        cancel();
//                    }
        Date d1=new Date(System.currentTimeMillis());
        System.out.println("开始延时："+d1);
try{
    Thread.sleep(5000);
}catch (Exception e){
    e.printStackTrace();
}
        Date d2=new Date(System.currentTimeMillis());
        System.out.println("结束延时："+d2);
        System.out.println("延时："+(d2.getTime()-d1.getTime())/1000+"秒");

                    //获取缓存中的截屏信息
                    String str = (String) redisTemplate.opsForValue().get(ScreenshotUploadReturnDto.cacheKey(index));
                    ScreenshotUploadReturnDto dto = JSONObject.parseObject(str, ScreenshotUploadReturnDto.class);//json转dto

                    if (dto != null) {
                        //构造返回给vue的dto
                        res.setFailed(false);
                        res.setId(1);
                        res.setUrl(dto.getMurl());
                        res.setMemo("获取截屏成功");
                    } else {
                        logger.warn("获取截屏失败");
                        res.setFailed(true);
                        res.setId(0);
                        res.setMemo("获取截屏失败");
                    }

//                    i++;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 5000, 5000);
        System.out.println("截图接口结束");
        return res;
    }

    @Override
    public VueReturnDto timing(Function2Dto function2Dto) throws BoeBoardServiceException {
        VueReturnDto res = new VueReturnDto();//存放结果

        if (function2Dto != null) {
            //消息通过消息队列发给android
            MqMessage msg = new MqMessage(MqMessage.CATEGORY_TIMING);
            msg.appendContent("starttime", function2Dto.getStarttime());
            msg.appendContent("endtime", function2Dto.getEndtime());
            msg.appendContent("cycles", function2Dto.getCycles());
            msg.appendContent("memo", "执行自动开关机");//备注
//            mqService.convertAndSend(Constants.QUE_TIMING, msg.stringfy());//msg.stringfy()转json
            mqService.convertSendAndReceive(Constants.QUE_TIMING, msg.stringfy());//msg.stringfy()转json

            //填充返回给vue的dto
            res.setFailed(false);
            res.setId(1);
            res.setMemo("发送自动开关机的消息成功");
        } else {
            logger.warn("发送自动开关机的消息失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("发送自动开关机的消息失败");
        }
        return res;
    }

    @Override
    public VueReturnDto restart() throws BoeBoardServiceException {
        VueReturnDto res = new VueReturnDto();//存放结果

        //消息通过消息队列发给android
        MqMessage msg = new MqMessage(MqMessage.CATEGORY_RESTART);
        msg.appendContent("memo", "执行重启系统");//备注
//            mqService.convertAndSend(Constants.QUE_RESTART, msg.stringfy());//msg.stringfy()转json
        mqService.convertSendAndReceive(Constants.QUE_RESTART, msg.stringfy());//msg.stringfy()转json

        //填充返回给vue的dto
        res.setFailed(false);
        res.setId(1);
        res.setMemo("发送重启系统的消息成功");

        return res;
    }

    @Override
    public VueReturnDto volume(Function4Dto function4Dto) throws BoeBoardServiceException {
        VueReturnDto res = new VueReturnDto();//存放结果

        if (function4Dto != null) {
            //消息通过消息队列发给android
            MqMessage msg = new MqMessage(MqMessage.CATEGORY_VOLUME);
            msg.appendContent("volume", function4Dto.getVolume());
            msg.appendContent("memo", "执行音量控制");//备注
//            mqService.convertAndSend(Constants.QUE_VOLUME, msg.stringfy());//msg.stringfy()转json
            mqService.convertSendAndReceive(Constants.QUE_VOLUME, msg.stringfy());//msg.stringfy()转json

            //填充返回给vue的dto
            res.setFailed(false);
            res.setId(1);
            res.setMemo("发送音量控制的消息成功");
        } else {
            logger.warn("发送音量控制的消息失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("发送音量控制的消息失败");
        }
        return res;
    }

    @Override
    public VueReturnDto brightness(Function5Dto function5Dto) throws BoeBoardServiceException {
        VueReturnDto res = new VueReturnDto();//存放结果

        if (function5Dto != null) {
            //消息通过消息队列发给android
            MqMessage msg = new MqMessage(MqMessage.CATEGORY_BRIGHTNESS);
            msg.appendContent("brightness", function5Dto.getBrightness());//亮度值
            msg.appendContent("memo", "执行亮度控制");//备注
//            mqService.convertAndSend(Constants.QUE_BRIGHTNESS, msg.stringfy());//msg.stringfy()转json
            mqService.convertSendAndReceive(Constants.QUE_BRIGHTNESS, msg.stringfy());//msg.stringfy()转json

            //填充返回给vue的dto
            res.setFailed(false);
            res.setId(1);
            res.setMemo("发送亮度控制的消息成功");
        } else {
            logger.warn("发送亮度控制的消息失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("发送亮度控制的消息失败");
        }
        return res;
    }

    @Override
    public VueReturnDto upgrade() throws BoeBoardServiceException {
        VueReturnDto res = new VueReturnDto();//存放结果

        //消息通过消息队列发给android
        MqMessage msg = new MqMessage(MqMessage.CATEGORY_UPGRADE);
        msg.appendContent("memo", "执行系统升级");//备注
//            mqService.convertAndSend(Constants.QUE_UPGRADE, msg.stringfy());//msg.stringfy()转json
        mqService.convertSendAndReceive(Constants.QUE_UPGRADE, msg.stringfy());//msg.stringfy()转json

        //填充返回给vue的dto
        res.setFailed(false);
        res.setId(1);
        res.setMemo("发送系统升级的消息成功");

        devicesRepository.UpdateDevice("已是最新");

        return res;
    }

    @Override
    public VueReturnDto clearcache() throws BoeBoardServiceException {
        VueReturnDto res = new VueReturnDto();//存放结果

        //消息通过消息队列发给android
        MqMessage msg = new MqMessage(MqMessage.CATEGORY_CLEARCACHE);
        msg.appendContent("memo", "执行清除缓存");//备注
//            mqService.convertAndSend(Constants.QUE_CLEARCACHE, msg.stringfy());//msg.stringfy()转json
        mqService.convertSendAndReceive(Constants.QUE_CLEARCACHE, msg.stringfy());//msg.stringfy()转json

        //填充返回给vue的dto
        res.setFailed(false);
        res.setId(1);
        res.setMemo("发送清除缓存的消息成功");

        return res;
    }

    @Override
    public DevicesDto edit(DevicesEditInputDto deviceEditInputDto) throws BoeBoardServiceException {
        DevicesDto res = new DevicesDto();//存放结果

        if (deviceEditInputDto != null) {
            //获取传入的数据
            long d_id = deviceEditInputDto.getD_id();
            String dname = deviceEditInputDto.getDname();
            String groupings = deviceEditInputDto.getGroupings();

            //查询是否存在该设备
            Devices device = devicesRepository.searchDeviceByDid(d_id);

            if (device != null) {//存在该设备
                //更新数据库
                devicesRepository.editDevice(dname, groupings, d_id);

                //填充返回的dto
                res.setFailed(false);
                res.setId(device.getId());
                res.setDname(dname);
                res.setGroupings(groupings);
                res.setMemo("设备编辑成功");
            } else {
                logger.warn("设备编辑失败");
                res.setFailed(true);
                res.setId(0);
                res.setMemo("设备编辑失败");
            }
        } else {
            logger.warn("设备编辑失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("设备编辑失败");
        }
        return res;
    }

    @Override
    public List<DevicesAllDto> getAll() throws BoeBoardServiceException {
        List<DevicesAllDto> res = new ArrayList<>();//存放结果
        int index = 1;//下标

        //查找数据库
        List<Devices> list = devicesRepository.findListOfDevices();//获取所有设备

        //遍历list，构造返回dto列表
        for (Devices item : list) {
            DevicesAllDto dto = new DevicesAllDto();
            dto.setFailed(false);
            dto.setId(index++);
            dto.setD_id(item.getId());
            dto.setDname(item.getDname());
            dto.setInst(item.getInst());
            dto.setGroupings(item.getGroupings());
            dto.setMac2(item.getMac2());
            dto.setDpi(item.getDpi());
            dto.setDstatus(item.getDstatus());
            dto.setVer1(item.getVer1());
            dto.setMemo("获取设备列表成功");
            dto.setD_update(item.getD_update());
            res.add(dto);
        }

        return res;
    }

    @Override
    public String upAndDown(String dstatus) throws BoeBoardServiceException {
        if (dstatus.equals("off") || dstatus.equals("relax") || dstatus.equals("playing")) {
            if (dstatus.equals("off")) {
                System.out.println("离线");//测试
                devicesRepository.upAndDown("离线");//更新数据库状态
            } else if (dstatus.equals("relax")) {
                System.out.println("空闲");//测试
                devicesRepository.upAndDown("空闲");//更新数据库状态
            } else if (dstatus.equals("playing")) {
                System.out.println("播放");//测试
                devicesRepository.upAndDown("播放");//更新数据库状态
            }

            //找当前设备（在数据库中为下标1）
            Devices devices = devicesRepository.findDeviceByAbsLoc();

            //存缓存
            if (devices != null) {
                ++index_log;
                DevicesUpAndDownDto dto = new DevicesUpAndDownDto();
                dto.setId(index_log);
                dto.setTime(new Date(System.currentTimeMillis()));
                dto.setContent("[设备]" + devices.getDname() + "上下线状态更新成功");
                redisTemplate.opsForValue().set(DevicesUpAndDownDto.cacheKey(index_log), JSONObject.toJSONString(dto),
                        2592000, TimeUnit.SECONDS);
                logger.warn("[设备]上下线状态更新成功[{}]", DevicesUpAndDownDto.cacheKey(index_log));
            }
        } else {
            //存缓存
            ++index_log;
            DevicesUpAndDownDto dto = new DevicesUpAndDownDto();
            dto.setId(index_log);
            dto.setTime(new Date(System.currentTimeMillis()));
            dto.setContent("[设备]上下线状态更新成功");
            redisTemplate.opsForValue().set(DevicesUpAndDownDto.cacheKey(index_log), JSONObject.toJSONString(dto),
                    2592000, TimeUnit.SECONDS);
            logger.warn("[设备]上下线状态更新成功[{}]", DevicesUpAndDownDto.cacheKey(index_log));
        }

        //获取缓存中的cnt
        String str = (String) redisTemplate.opsForValue().get(CntLogDeviceDto.cacheKey(1));
        CntLogDeviceDto dto = JSONObject.parseObject(str, CntLogDeviceDto.class);//json转dto

        int nowCnt = 0;
        if (dto != null) {
            nowCnt = dto.getCnt();
        }

        //存日志数量
        CntLogDeviceDto cnt = new CntLogDeviceDto();
        cnt.setCnt(++nowCnt);
        redisTemplate.opsForValue().set(CntLogDeviceDto.cacheKey(1), JSONObject.toJSONString(cnt),
                2592000, TimeUnit.SECONDS);
        logger.warn("[设备]上下线状态更新成功[{}]", CntLogDeviceDto.cacheKey(1));

        return "设备上下线状态更新成功";
    }

    @Override
    public ApkInfoDto GetApkInfo() throws BoeBoardServiceException {
        AndroidApk apkinfo = apkRepository.GetApkInfo();
        ApkInfoDto dto = new ApkInfoDto();
        dto.setApk_url(apkinfo.getApk_url());
        dto.setJson_url(apkinfo.getJson_url());
        return dto;
    }
}
