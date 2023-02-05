package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//添加计划传入的dto
@Data
public class SchemeAddInputDto implements Serializable {
    private String sname;//计划名称
    private int strategy;//循环策略
    private int cycles;//循环周期
    private String starttime;//开始时间
    private String endtime;//结束时间
//    private String duration;//循环时间段
    private int playmode;//播放模式
    private int synchronize;//多屏同步
    private String startday;//开始日期
    private String endday;//结束日期
//    private String playdate;//播放日期【按时段播放：日期；持续播放：”持续播放“】
    private String author;//计划作者，审核人的username【默认为author】
    private long p_id;//节目id
    private long d_id;//设备id
}
