package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;
//获取所有设备、获取设备数量 传入的dto
@Data
public class DevicesGetAllInputDto implements Serializable {
    private boolean flag;//获取所有设备的指令标志
}
