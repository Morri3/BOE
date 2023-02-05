package com.team.boeboard.form.devices.screenshot;

import lombok.Data;

import java.io.Serializable;
//屏幕截图 传入的dto
@Data
public class Function1Dto implements Serializable {
    private long id;
    private boolean flag;//标记点击了开始截屏的按钮
}
