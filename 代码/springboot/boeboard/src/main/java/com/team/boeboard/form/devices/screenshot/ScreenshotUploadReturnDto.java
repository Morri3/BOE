package com.team.boeboard.form.devices.screenshot;

import com.team.boeboard.utils.BoeBoardUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//上传截图到MinIO 返回给android的dto
@Data
public class ScreenshotUploadReturnDto implements Serializable {
    public static final String KEY_PREFIX = "Screenshot";//缓存前缀

    private long id;

    //返回图片的相关信息
    private String murl;//截屏url
    private int category;//截屏种类
    private String msize;//截屏大小
    private Date createtime;//创建时间
    private String mstatus;//截屏状态
    private String name;//截屏名称（非完整的url）

    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】

    //生成缓存的key
    public static String cacheKey(long id) {
        return BoeBoardUtils.generateCacheKey(KEY_PREFIX, String.valueOf(id));
    }
}
