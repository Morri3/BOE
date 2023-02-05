package com.team.boeboard.form.devices.screenshot;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//vue获取截屏 调用的dto；截屏 返回给vue的dto
@Data
public class ScreenshotGetDto implements Serializable {
//    public static final String KEY_PREFIX = "Getscreenshot";//缓存前缀

    private long id;

    //返回图片的相关信息
    private String url;//截屏url
//    private int category;//截屏种类
//    private String msize;//截屏大小
//    private Date createtime;//创建时间
//    private String mstatus;//截屏状态
//    private String name;//文件名（非完整url）

    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】

//    public static String cacheKey(long id) {//生成缓存的key
//        return BoeBoardUtils.generateCacheKey(KEY_PREFIX, String.valueOf(id));
//    }
}
