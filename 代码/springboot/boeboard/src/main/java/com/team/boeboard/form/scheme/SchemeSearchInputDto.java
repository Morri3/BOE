package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
//查询计划 传入的dto
@Data
public class SchemeSearchInputDto implements Serializable {
    private String sname;//计划名称
    private String sstatus;//计划状态
}
