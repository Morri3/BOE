package com.team.boeboard.entity;

import javax.persistence.*;

@Entity
@Table(name = "inst")
public class Inst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_id", nullable = false)
    private long id;

    @Column(name = "iname")
    private String iname;

    @Column(name = "memo")
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}