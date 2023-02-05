package com.team.boeboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "announce")
public class Announce {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id", nullable = false)
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "text_color")
    private String text_color;

    @Column(name = "text_size")
    private int text_size;

    @Column(name = "background_color")
    private String background_color;

    @Column(name = "start_time")
    private String start_time;

    @Column(name = "finish_time")
    private String finish_time;

    @Column(name = "astatus")
    private String astatus;

    @Column(name = "author")
    private String author;

    @Column(name = "createtime")
    private Date createtime;


    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAstatus() {
        return astatus;
    }

    public void setAstatus(String astatus) {
        this.astatus = astatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public int getText_size() {
        return text_size;
    }

    public void setText_size(int text_size) {
        this.text_size = text_size;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }
}