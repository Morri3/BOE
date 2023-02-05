package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
//计划详情 返回给vue的dto
@Data
public class SchemeInfoDto implements Serializable {
    private long id;
    private long s_id;//计划id
    private String sname;//计划名称
    private String playdate;//播放日期【按时段播放：日期；持续播放：”持续播放“】
    private int playmode;//播放模式
    private int strategy;//播放策略
    private int synchronize;//多屏同步
    private String sstatus;//计划状态
    private Date createtime;//创建时间
    private int cycles;//循环周期
    private String duration;//循环时间段【starttime+endtime组合】
    private long p_id;//节目id
    private int plength;//节目时长
    private String murl;//节目中的素材url
//    private Date updatetime;//更新时间

    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
