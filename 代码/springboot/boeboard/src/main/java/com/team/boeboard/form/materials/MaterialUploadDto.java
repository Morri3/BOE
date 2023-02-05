package com.team.boeboard.form.materials;

import lombok.Data;

import java.io.Serializable;

@Data
public class MaterialUploadDto implements Serializable {
    private String file_name;
    private String url;
}
