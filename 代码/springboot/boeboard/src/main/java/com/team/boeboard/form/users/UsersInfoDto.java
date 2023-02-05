package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
//获取用户信息、启用、停用、删除用户 传入的dto
@Data
public class UsersInfoDto implements Serializable {
    private long id;
    private String username;//账户名
}
