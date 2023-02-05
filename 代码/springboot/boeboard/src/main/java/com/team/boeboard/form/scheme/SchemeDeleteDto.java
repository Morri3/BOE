package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
//删除计划返回给vue的dto
@Data
public class SchemeDeleteDto implements Serializable {
    private long id;
    private String sname;//计划名称
    private Date updatetime;//更新时间
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
