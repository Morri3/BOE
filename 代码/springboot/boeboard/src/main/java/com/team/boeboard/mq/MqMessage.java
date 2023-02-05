package com.team.boeboard.mq;

import com.team.boeboard.utils.BoeBoardUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//消息队列发送的消息类
public class MqMessage implements Serializable {
    //使用基础模式
    //发布计划
    public static final String CATEGORY_SCHEME = "scheme";
    public static final String CATEGORY_SCHEMEVIDEO = "video";
    //七个设备控制
    public static final String CATEGORY_SCREENSHOT = "screenshot";
    public static final String CATEGORY_TIMING = "timing";
    public static final String CATEGORY_RESTART = "restart";
    public static final String CATEGORY_VOLUME = "volume";
    public static final String CATEGORY_BRIGHTNESS = "brightness";
    public static final String CATEGORY_UPGRADE = "upgrade";
    public static final String CATEGORY_CLEARCACHE = "clearcache";

    //公告消息
    public static final String CATEGORY_ANNOUNCE = "announce";

    private String category;
    private Map<String, Object> content = new HashMap<>();

    public MqMessage() {
    }

    public MqMessage(String category) {
        this.category = category;
    }

    //添加msg内容
    public void appendContent(String key, Object value) {
        content.put(key, value);
    }

    //转json的方法
    public String stringfy() {
        return BoeBoardUtils.beanToJson(this);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
}
