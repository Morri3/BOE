package com.team.boeboard.form.groupings;

import lombok.Data;

import java.io.Serializable;
//删除分组 传入的dto
@Data
public class GroupingsDeleteInputDto implements Serializable {
    private String gname;
}
