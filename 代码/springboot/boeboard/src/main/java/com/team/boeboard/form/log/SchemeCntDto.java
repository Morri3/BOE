package com.team.boeboard.form.log;

import com.team.boeboard.utils.BoeBoardUtils;
import lombok.Data;

import java.io.Serializable;

//计划缓存数量
@Data
public class SchemeCntDto implements Serializable {
    public static final String KEY_PREFIX = "cnt_scheme";//缓存前缀

    private int cnt_scheme;//数量

    public static String cacheKey(long id) {//生成缓存的key
        return BoeBoardUtils.generateCacheKey(KEY_PREFIX, String.valueOf(id));
    }
}
