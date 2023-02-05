package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
//复制计划 传入的dto
@Data
public class SchemeReproduceInputDto implements Serializable {
    long s_id;//计划id
    long u_id;//用户id
}
