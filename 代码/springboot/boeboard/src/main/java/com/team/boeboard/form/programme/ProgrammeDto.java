package com.team.boeboard.form.programme;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
public class ProgrammeDto implements Serializable {
    private String p_name;                  //节目名称
    private String dpi;                     //节目分辨率
    private List<MaterialsDto> materials;   //素材url列表
    private int plength;                    //节目时长
    private String psize;                   //素材大小
}
