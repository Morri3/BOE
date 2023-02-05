package com.team.boeboard.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "scheme")
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "d_id", nullable = false)
    private Devices d;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "p_id", nullable = false)
    private Programme p;

    @Column(name = "sname")
    private String sname;

    @Column(name = "sstatus")
    private String sstatus;

    @Column(name = "playmode")
    private int playmode;

    @Column(name = "cycles")
    private int cycles;

    @Column(name = "strategy")
    private int strategy;

    @Column(name = "synchronize")
    private int synchronize;

    @Column(name = "startday")
    private Date startday;

    @Column(name = "endday")
    private Date endday;

    @Column(name = "starttime")
    private Date starttime;

    @Column(name = "endtime")
    private Date endtime;

    @Column(name = "author")
    private String author;

    @Column(name = "reviewer")
    private String reviewer;

    @Column(name = "updatetime")
    private Date updatetime;

    @Column(name = "createtime")
    private Date createtime;

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndday() {
        return endday;
    }

    public void setEndday(Instant Date) {
        this.endday = endday;
    }

    public Date getStartday() {
        return startday;
    }

    public void setStartday(Date startday) {
        this.startday = startday;
    }

    public int getSynchronize() {
        return synchronize;
    }

    public void setSynchronize(int synchronize) {
        this.synchronize = synchronize;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public int getPlaymode() {
        return playmode;
    }

    public void setPlaymode(int playmode) {
        this.playmode = playmode;
    }

    public String getSstatus() {
        return sstatus;
    }

    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Programme getP() {
        return p;
    }

    public void setP(Programme p) {
        this.p = p;
    }

    public Devices getD() {
        return d;
    }

    public void setD(Devices d) {
        this.d = d;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}