package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;

//屏幕截图接口1/其他六个控制功能 返回给vue的dto
@Data
public class VueReturnDto implements Serializable {
    private long id;
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
