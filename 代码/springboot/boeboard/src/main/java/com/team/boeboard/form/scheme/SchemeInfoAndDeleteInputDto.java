package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
//获取计划详情、删除计划 传入的dto
@Data
public class SchemeInfoAndDeleteInputDto implements Serializable {
    private long id;
    private long s_id;//计划id
}
