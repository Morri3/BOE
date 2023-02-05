package com.team.boeboard.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class MaterialUploadDto implements Serializable {
    private String file_name;
    private String url;
}
