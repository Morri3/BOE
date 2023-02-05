package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;
//添加设备 传入的dto
@Data
public class DevicesAddInputDto implements Serializable {
    private long id;
    private String dname;//设备名
    private String mac2;//无线mac地址
    private String inst;//所属机构（名称）
    private String sn;//设备sn
}