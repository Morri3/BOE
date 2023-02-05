package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
//获取所有计划 传入的dto
@Data
public class SchemeAllInputDto implements Serializable {
    private long id;
    private boolean flag;//获取所有计划的指令标志
}
