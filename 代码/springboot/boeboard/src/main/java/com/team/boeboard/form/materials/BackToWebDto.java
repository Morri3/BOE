package com.team.boeboard.form.materials;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BackToWebDto implements Serializable {
    private String mname;       //素材名称
    private String murl;        //素材url
    private String msize;       //素材大小
    private String dpi;         //素材dpi
    private String author;      //上传的作者
    private Date updatetime;    //更新时间
    private Date createtime;    //创建的时间
    private String mstatus;     //素材状态
    private int category;       //素材种类
}
