package com.team.boeboard.entity;

import javax.persistence.*;

@Entity
@Table(name = "matl_prog")
public class MatlProg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mp_id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "m_id", nullable = false)
    private Materials m;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "p_id", nullable = false)
    private Programme p;

    public Programme getP() {
        return p;
    }

    public void setP(Programme p) {
        this.p = p;
    }

    public Materials getM() {
        return m;
    }

    public void setM(Materials m) {
        this.m = m;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}