package com.team.boeboard.form.log;

import com.team.boeboard.utils.BoeBoardUtils;
import lombok.Data;

import java.io.Serializable;

//存日志数量
@Data
public class CntLogDeviceDto implements Serializable {
    public static final String KEY_PREFIX = "cnt_device_log";//缓存前缀

    private int cnt;//数量

    public static String cacheKey(long id) {//生成缓存的key
        return BoeBoardUtils.generateCacheKey(KEY_PREFIX, String.valueOf(id));
    }
}
