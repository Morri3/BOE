package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
//用户登录传入的dto
@Data
public class LoginDto implements Serializable {
    private long id;
    private String username;//账户名
    private String userpwd;//密码
}
