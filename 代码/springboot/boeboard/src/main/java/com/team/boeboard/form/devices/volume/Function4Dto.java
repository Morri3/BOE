package com.team.boeboard.form.devices.volume;

import lombok.Data;

import java.io.Serializable;

//音量控制 传入的dto
@Data
public class Function4Dto implements Serializable {
    private int volume;//音量值
}