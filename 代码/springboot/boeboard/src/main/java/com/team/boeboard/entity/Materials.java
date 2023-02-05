package com.team.boeboard.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "materials")
public class Materials {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id", nullable = false)
    private long id;

    @Column(name = "mname")
    private String mname;

    @Column(name = "murl")
    private String murl;

    @Column(name = "category")
    private int category;

    @Column(name = "dpi")
    private String dpi;

    @Column(name = "msize")
    private String msize;

    @Column(name = "author")
    private String author;

    @Column(name = "updatetime")
    private Date updatetime;

    @Column(name = "createtime")
    private Date createtime;

    @Column(name = "mstatus")
    private String mstatus;

    @OneToMany(mappedBy = "m")
    private Set<MatlProg> matlProgs = new LinkedHashSet<>();

    public Set<MatlProg> getMatlProgs() {
        return matlProgs;
    }

    public void setMatlProgs(Set<MatlProg> matlProgs) {
        this.matlProgs = matlProgs;
    }

    public String getMstatus() {
        return mstatus;
    }

    public void setMstatus(String mstatus) {
        this.mstatus = mstatus;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMsize() {
        return msize;
    }

    public void setMsize(String msize) {
        this.msize = msize;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}