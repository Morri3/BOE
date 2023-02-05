package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
//获取用户信息、编辑用户信息 返回给vue的dto
//编辑用户信息 传入的dto
@Data
public class UsersEditDto implements Serializable {
    private long id;
    private String username;//账户名
    private String userpwd;//密码
    private String organ;//所属机构
    private String userrole;//所属角色
    private String ustatus;//账号状态
    private String realname;//真实姓名
    private String useremail;//邮箱
    private String phone;//手机号
    private Date updatetime;//更新时间
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}