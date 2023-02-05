package com.team.boeboard.form.devices.timing;

import lombok.Data;

import java.io.Serializable;

//定时开关机 传入的dto
@Data
public class Function2Dto implements Serializable {
    private String starttime;//开始时间
    private String endtime;//结束时间
    private int cycles;//循环周期
}