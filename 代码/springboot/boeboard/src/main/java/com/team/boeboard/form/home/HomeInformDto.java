package com.team.boeboard.form.home;

import com.team.boeboard.form.log.LogDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class HomeInformDto implements Serializable {
    private int programme_num;  //节目数量
    private String pic_size;       //图片大小
    private String video_size;     //视频大小
    private String music_size;     //音乐大小
    private String material_size;//素材总大小
    private int scheme_num;//计划数量
    private int device_num;//设备数量
    private int cnt1;//离线状态的设备数量
    private int cnt2;//播放状态的设备数量
    private int cnt3;//空闲状态的设备数量
    private Map<String, Integer> inst_num;//机构数量
    private Map<String, Integer> groupings_num;//分组数量
    private List<LogDto> logs;//事件日志
}
