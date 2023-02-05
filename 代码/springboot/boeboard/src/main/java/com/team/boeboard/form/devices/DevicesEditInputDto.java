package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;
//编辑设备 传入的dto
@Data
public class DevicesEditInputDto implements Serializable {
    private long id;
    private long d_id;//设备id
    private String dname;//设备名称
    private String groupings;//所在分组
}
