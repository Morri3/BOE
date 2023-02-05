package com.team.boeboard.form.programme;

import lombok.Data;

import java.io.Serializable;

@Data
public class MaterialsDto implements Serializable {
    private String mname;       //素材名称
    private String murl;        //素材文件访问url
    private int category;       //素材类型
}
