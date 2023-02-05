package com.team.boeboard.form.devices.screenshot;

import lombok.Data;

import java.io.Serializable;

//屏幕截图接口2返回给android的dto
@Data
public class ScreenshotAndroidReturnDto implements Serializable {
    private long id;
    private String url;//图片url
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
