package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;

//获取设备状态 返回给vue的dto
@Data
public class DevicesStatusDto implements Serializable {
    private long id;
    private int cnt1;//离线状态数量
    private int cnt2;//播放状态数量
    private int cnt3;//空闲状态数量
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
