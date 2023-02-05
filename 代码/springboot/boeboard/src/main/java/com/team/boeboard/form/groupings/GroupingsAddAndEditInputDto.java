package com.team.boeboard.form.groupings;

import lombok.Data;

import java.io.Serializable;
//添加分组、编辑分组 传入的dto
@Data
public class GroupingsAddAndEditInputDto implements Serializable {
    private String gname;//分组名称
    private String inst;//所属机构
    private String memo;//分组描述
    private String dname;//设备名称
}
