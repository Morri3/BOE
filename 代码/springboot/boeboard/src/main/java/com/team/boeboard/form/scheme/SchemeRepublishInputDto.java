package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;

//重新发布计划 传入的dto
@Data
public class SchemeRepublishInputDto implements Serializable {
    long s_id;//计划id
    long u_id;//用户id
}
