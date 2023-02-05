package com.team.boeboard.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "programme")
public class Programme {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id", nullable = false)
    private long id;

    @Column(name = "pname")
    private String pname;

    @Column(name = "dpi")
    private String dpi;

    @Column(name = "plength")
    private int plength;

    @Column(name = "psize")
    private String psize;

    @Column(name = "pstatus")
    private String pstatus;

    @Column(name = "author")
    private String author;

    @Column(name = "updatetime")
    private Date updatetime;

    @Column(name = "createtime")
    private Date createtime;

    @Column(name = "materials")
    private String materials;

    @OneToMany(mappedBy = "p")
    private Set<MatlProg> matlProgs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "p")
    private Set<Scheme> schemes = new LinkedHashSet<>();

    public Set<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(Set<Scheme> schemes) {
        this.schemes = schemes;
    }

    public Set<MatlProg> getMatlProgs() {
        return matlProgs;
    }

    public void setMatlProgs(Set<MatlProg> matlProgs) {
        this.matlProgs = matlProgs;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
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

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getPsize() {
        return psize;
    }

    public void setPsize(String psize) {
        this.psize = psize;
    }

    public int getPlength() {
        return plength;
    }

    public void setPlength(int plength) {
        this.plength = plength;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}