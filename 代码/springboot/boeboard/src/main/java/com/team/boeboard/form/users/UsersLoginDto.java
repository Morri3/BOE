package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
//用户登录 返回给vue的dto
@Data
public class UsersLoginDto implements Serializable {
    private long id;
    private String username;//账户名
    private String phone;//手机号
    private String token;//token
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}