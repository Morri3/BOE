package com.team.boeboard.form.log;

import lombok.Data;

import java.io.Serializable;
//事件日志子类
@Data
public class LogDto implements Serializable {
    private String time;//日期
    private String content;//事件内容
}
