package com.team.boeboard.entity;

import javax.persistence.*;

@Entity
@Table(name = "groupings")
public class Groupings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g_id", nullable = false)
    private long id;

    @Column(name = "gname")
    private String gname;

    @Column(name = "memo")
    private String memo;

    @Column(name = "inst")
    private String inst;

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}