package com.team.boeboard.form.devices.screenshot;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

//接收来自android的屏幕截图 传入的dto
@Data
public class ScreenshotUploadDto implements Serializable {
    private long id;
    private MultipartFile photo;//图片
}

