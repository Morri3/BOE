package com.team.boeboard.form.devices.brightness;

import lombok.Data;

import java.io.Serializable;

//亮度控制 传入的dto
@Data
public class Function5Dto implements Serializable {
    private int brightness;//亮度值
}