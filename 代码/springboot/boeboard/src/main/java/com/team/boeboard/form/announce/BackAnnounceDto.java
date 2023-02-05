package com.team.boeboard.form.announce;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BackAnnounceDto implements Serializable {
    private String content;
    private String start_time;
    private String astatus;
    private String author;
    private Date createtime;
}
