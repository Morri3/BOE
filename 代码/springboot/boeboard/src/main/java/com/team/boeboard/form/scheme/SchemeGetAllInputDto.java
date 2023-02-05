package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;

//获取计划数量 传入的dto
@Data
public class SchemeGetAllInputDto implements Serializable {
    private boolean flag;//获取所有计划的指令标志
}
