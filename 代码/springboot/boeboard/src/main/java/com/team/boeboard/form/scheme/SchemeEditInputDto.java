package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//编辑计划 传入的dto
@Data
public class SchemeEditInputDto implements Serializable {
    private long s_id;//计划id
    private String sname;//计划名称
    private int strategy;//循环策略
    private int cycles;//循环周期
    private String starttime;//开始时间
    private String endtime;//结束时间
    private int playmode;//播放模式
    private int synchronize;//多屏同步
    private String startday;//开始日期
    private String endday;//结束日期
    private long p_id;//节目id
    private long d_id;//设备id
}
