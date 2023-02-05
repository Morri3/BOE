package com.team.boeboard.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
public class SchemeAddDto implements Serializable {
    private String pname;       //节目名称
    private int time_second;    //轮播时间
    private List<String> m_urls;       //素材url
}
