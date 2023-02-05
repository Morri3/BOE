package com.team.boeboard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.team.boeboard.entity.Devices;
import com.team.boeboard.entity.Materials;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.devices.DevicesAllDto;
import com.team.boeboard.form.devices.DevicesUpAndDownDto;
import com.team.boeboard.form.home.HomeInformDto;
import com.team.boeboard.form.log.CntLogDeviceDto;
import com.team.boeboard.form.log.CntLogDto;
import com.team.boeboard.form.log.LogDto;
import com.team.boeboard.form.log.SchemeAddLogDto;
import com.team.boeboard.repository.DevicesRepository;
import com.team.boeboard.repository.MaterialRepository;
import com.team.boeboard.repository.ProgrammeRepository;
import com.team.boeboard.repository.SchemeRepository;
import com.team.boeboard.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private ProgrammeRepository programmeRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private SchemeRepository schemeRepository;
    @Autowired
    private DevicesRepository devicesRepository;

    static double total_size = 0;       //素材总大小
    static double pic_size = 0;         //图片大小
    static double video_size = 0;       //视频大小
    static double music_size = 0;       //音乐大小

    @Autowired
    private RedisTemplate redisTemplate;//缓存

    @Override
    public HomeInformDto GetHomeInfo() throws BoeBoardServiceException {

        MaterialServiceImpl materialService = new MaterialServiceImpl();


        HomeInformDto res = new HomeInformDto();//存放结果

        //TODO 获取节目数量
        int programme_num = programmeRepository.CountProgramme();

        //获取素材数量(图片、视频、音乐)

        List<Materials> materials = materialRepository.findAllMaterial();
        materials.forEach(material -> {
            String mname = material.getMname();
            String extName = mname.substring(mname.indexOf(".") + 1);
            double material_size = this.GetSingleSize(material.getMsize());

            if (extName.equals("jpg") || extName.equals("png") || extName.equals("jpeg") || extName.equals("gif")) {
                pic_size += material_size;
            } else if (extName.equals("mp3") || extName.equals("wav") || extName.equals("cda")) {
                music_size += material_size;
            } else {
                video_size += material_size;
            }
        });

        String Pic_Size = materialService.setSize((long) pic_size);
        String Video_Size = materialService.setSize((long) video_size);
        String Music_Size = materialService.setSize((long) music_size);


        //TODO 获取素材总大小
        List<String> sizelist = materialRepository.SelectMsize();
        sizelist.forEach(size -> {
            double single_size = this.GetSingleSize(size);
            total_size += single_size;
        });

        String totalsize = materialService.setSize((long) total_size);

        //TODO 获取计划数量
        int scheme_num = schemeRepository.countScheme();

        //TODO 获取设备数量
        int device_num = devicesRepository.countDevices();

        //TODO 获取设备状态
        List<DevicesAllDto> list = new ArrayList<>();//存放所有设备的dto结果
        int index = 1;//下标

        //查找数据库
        List<Devices> devices = devicesRepository.findListOfDevices();//获取所有设备

        //遍历list，构造返回dto列表
        for (Devices item : devices) {
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
            list.add(dto);
        }
        int cnt1 = 0, cnt2 = 0, cnt3 = 0;//三种状态的设备数量
        if (list.size() > 0) {
            //遍历list
            for (DevicesAllDto item : list) {
                if ((item.getDstatus()).equals("离线")) {
                    cnt1++;
                } else if ((item.getDstatus()).equals("播放")) {
                    cnt2++;
                } else if ((item.getDstatus()).equals("空闲")) {
                    cnt3++;
                }
            }
        }

        //TODO 获取设备分布
        //机构数量
        Map<String, Integer> inst_num = devicesRepository.inst();
        //分组数量
        Map<String, Integer> groupings_num = devicesRepository.groupings();

        //TODO 获取事件日志
        List<LogDto> logs = new ArrayList<>();

        //获取【计划】事件日志数量
        int cnt = 0;
        String str = (String) redisTemplate.opsForValue().get(CntLogDto.cacheKey(1));
        CntLogDto dto = JSONObject.parseObject(str, CntLogDto.class);//json转dto
        if (dto != null) {
            cnt = dto.getCnt();
        }

        //把每条日志加到map中
        for (int i = 1; i <= cnt; i++) {
            String str2 = (String) redisTemplate.opsForValue().get(SchemeAddLogDto.cacheKey(i));
            SchemeAddLogDto dto2 = JSONObject.parseObject(str2, SchemeAddLogDto.class);//json转dto
            if (dto2 != null) {
                LogDto log = new LogDto();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                log.setTime(sdf.format(dto2.getTime()));
                log.setContent(dto2.getContent());
                logs.add(log);
            }
        }

        //获取【设备】事件日志数量
        int cnt_device = 0;
        String str2 = (String) redisTemplate.opsForValue().get(CntLogDeviceDto.cacheKey(1));
        CntLogDeviceDto dto2 = JSONObject.parseObject(str2, CntLogDeviceDto.class);//json转dto
        if (dto2 != null) {
            cnt_device = dto2.getCnt();
        }

        //把每条日志加到map中
        for (int i = 1; i <= cnt_device; i++) {
            String str3 = (String) redisTemplate.opsForValue().get(DevicesUpAndDownDto.cacheKey(i));
            DevicesUpAndDownDto dto3 = JSONObject.parseObject(str3, DevicesUpAndDownDto.class);//json转dto
            if (dto3 != null) {
                LogDto log = new LogDto();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                log.setTime(sdf.format(dto3.getTime()));
                log.setContent(dto3.getContent());
                logs.add(log);
            }
        }

        //列表倒序
        Collections.reverse(logs);

        //TODO 构造返回给vue的dto
        res.setProgramme_num(programme_num);
        res.setPic_size(Pic_Size);
        res.setMusic_size(Music_Size);
        res.setVideo_size(Video_Size);
        res.setMaterial_size(totalsize);
        res.setScheme_num(scheme_num);
        res.setDevice_num(device_num);
        res.setCnt1(cnt1);
        res.setCnt2(cnt2);
        res.setCnt3(cnt3);
        res.setInst_num(inst_num);
        res.setGroupings_num(groupings_num);
        res.setLogs(logs);


        total_size = 0;
        pic_size = 0;
        music_size = 0;
        video_size = 0;
        return res;
    }

    //获取单个素材的大小
    public double GetSingleSize(String size) {
        double single_size = 0;
        int index = 0;
        while (size.charAt(index) != 'M' && size.charAt(index) != 'K') {
            index++;
        }
        double num = Double.valueOf(size.substring(0, index));
        if (size.charAt(index) == 'M') {
            single_size = num * 1024 * 1024;
        } else if (size.charAt(index) == 'K') {
            single_size = num * 1024;
        }
        return single_size;
    }
}
