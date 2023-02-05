package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
//添加用户 返回给vue的dto
@Data
public class UsersDto implements Serializable {
    private long id;
    private String username;//账户名
    private String realname;//真实姓名
    private String useremail;//邮箱
    private String phone;//手机号
    private int category;//用户类型
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
