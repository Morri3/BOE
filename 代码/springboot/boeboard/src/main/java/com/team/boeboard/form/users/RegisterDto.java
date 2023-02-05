package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
//添加用户传入的dto
@Data
public class RegisterDto implements Serializable {
    private long id;
    private String username;//账户名
    private String userpwd;//密码
    private String organ;//所属机构
    private String userrole;//所属角色
    private String ustatus;//账号状态
    private String realname;//真实姓名
    private String useremail;//邮箱
    private String phone;//手机号
}
