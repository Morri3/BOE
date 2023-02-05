package com.team.boeboard.form.scheme;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//
@Data
public class SchemeAddDto implements Serializable {
    private long id;
    private String pname;       //节目名称
    private int time_second;    //轮播时间
    private String m_url;       //素材url
}
