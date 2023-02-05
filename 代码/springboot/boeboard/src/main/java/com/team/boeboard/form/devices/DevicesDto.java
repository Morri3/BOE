package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;
//添加设备 返回给vue的dto
@Data
public class DevicesDto implements Serializable {
    private long id;
    private String dname;//设备名称
    private String inst;//所属机构
    private String groupings;//所属分组
    private String mac2;//无线mac地址
    private String dpi;//设备分辨率
    private String dstatus;//设备状态
    private String ver1;//系统版本
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
