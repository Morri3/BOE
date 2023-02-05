package com.team.boeboard.form.users;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//启用、停用、删除用户 返回给vue的dto
@Data
public class UsersStatusDto implements Serializable {
    private long id;
    private String username;//账户名
    private Date updatetime;//更新时间
    private boolean isFailed;//是否发生错误
    private String memo;//备注【也存放错误信息】
}
