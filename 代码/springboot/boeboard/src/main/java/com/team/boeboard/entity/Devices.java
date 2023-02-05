package com.team.boeboard.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "devices")
public class Devices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "d_id", nullable = false)
    private long id;

    @Column(name = "dname")
    private String dname;

    @Column(name = "inst")
    private String inst;

    @Column(name = "groupings")
    private String groupings;

    @Column(name = "dstatus")
    private String dstatus;

    @Column(name = "dmodel")
    private String dmodel;

    @Column(name = "ver1")
    private String ver1;

    @Column(name = "dip")
    private String dip;

    @Column(name = "ver2")
    private String ver2;

    @Column(name = "mac1")
    private String mac1;

    @Column(name = "mac2")
    private String mac2;

    @Column(name = "ram")
    private String ram;

    @Column(name = "sn")
    private String sn;

    @Column(name = "storespace")
    private String storespace;

    @Column(name = "lat")
    private Date lat;

    @Column(name = "createtime")
    private Date createtime;

    @Column(name = "dpi")
    private String dpi;

    @Column(name = "instlarea")
    private String instlarea;

    @Column(name = "showway")
    private String showway;

    @Column(name = "d_update")
    private String d_update;

    @OneToMany(mappedBy = "d")
    private Set<Scheme> schemes = new LinkedHashSet<>();

    public Set<Scheme> getSchemes() {
        return schemes;
    }

    public void setSchemes(Set<Scheme> schemes) {
        this.schemes = schemes;
    }

    public String getShowway() {
        return showway;
    }

    public void setShowway(String showway) {
        this.showway = showway;
    }

    public String getInstlarea() {
        return instlarea;
    }

    public void setInstlarea(String instlarea) {
        this.instlarea = instlarea;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getLat() {
        return lat;
    }

    public void setLat(Date lat) {
        this.lat = lat;
    }

    public String getStorespace() {
        return storespace;
    }

    public void setStorespace(String storespace) {
        this.storespace = storespace;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getMac2() {
        return mac2;
    }

    public void setMac2(String mac2) {
        this.mac2 = mac2;
    }

    public String getMac1() {
        return mac1;
    }

    public void setMac1(String mac1) {
        this.mac1 = mac1;
    }

    public String getVer2() {
        return ver2;
    }

    public void setVer2(String ver2) {
        this.ver2 = ver2;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getVer1() {
        return ver1;
    }

    public void setVer1(String ver1) {
        this.ver1 = ver1;
    }

    public String getDmodel() {
        return dmodel;
    }

    public void setDmodel(String dmodel) {
        this.dmodel = dmodel;
    }

    public String getDstatus() {
        return dstatus;
    }

    public void setDstatus(String dstatus) {
        this.dstatus = dstatus;
    }

    public String getGroupings() {
        return groupings;
    }

    public void setGroupings(String groupings) {
        this.groupings = groupings;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getD_update() {
        return d_update;
    }

    public void setD_update(String d_update) {
        this.d_update = d_update;
    }
}