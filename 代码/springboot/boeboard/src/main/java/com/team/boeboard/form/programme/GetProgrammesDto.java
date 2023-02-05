package com.team.boeboard.form.programme;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class GetProgrammesDto implements Serializable {
    private long p_id;
    private String pname;
    private String dpi;
    private int plength;
    private String psize;
    private String pstatus;
    private String author;
    private Date updatetime;
    private Date createtime;
    private List<MaterialsDto> materials;
}
