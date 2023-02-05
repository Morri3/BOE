package com.team.boeboard.form.groupings;

import lombok.Data;

import java.io.Serializable;
//添加分组、编辑分组 返回的dto
@Data
public class GroupingsDto implements Serializable {
    private long g_id;//分组id
    private String gname;//分组名称
    private String inst;//所属机构
    private String memo;//分组描述
}
