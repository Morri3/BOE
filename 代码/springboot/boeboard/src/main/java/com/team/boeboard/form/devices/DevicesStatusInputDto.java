package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;
//获取设备状态 传入的dto
@Data
public class DevicesStatusInputDto implements Serializable {
    private boolean flag;//标记
}
