package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

//设备分布的两个接口 返回给vue的dto
@Data
public class DevicesCntDto implements Serializable {
    private Map<String, Integer> cnt;//Map存储
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
