package com.team.boeboard.form.devices;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApkInfoDto implements Serializable {
    private String apk_url;
    private String json_url;
}
