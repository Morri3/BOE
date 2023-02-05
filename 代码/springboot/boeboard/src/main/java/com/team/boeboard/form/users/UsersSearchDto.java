package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
//查询用户传入的dto
@Data
public class UsersSearchDto implements Serializable {
    private long id;
    private String username;//账户名
    private String organ;//所属机构
    private String userrole;//所属角色
    private String ustatus;//账号状态
}
