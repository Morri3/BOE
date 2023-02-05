package com.team.boeboard.form.devices;

import com.team.boeboard.utils.BoeBoardUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//设备上下线状态更新 存入缓存的dto
@Data
public class DevicesUpAndDownDto implements Serializable {
    public static final String KEY_PREFIX = "devices_log";//缓存前缀

    private long id;
    private Date time;//时间
    private String content;//内容

    public static String cacheKey(long id) {//生成缓存的key
        return BoeBoardUtils.generateCacheKey(KEY_PREFIX, String.valueOf(id));
    }
}
