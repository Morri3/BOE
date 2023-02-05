package com.team.boeboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "android_apk")
public class AndroidApk implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "apk_url")
    private String apk_url;

    @Column(name = "json_url")
    private String json_url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getJson_url() {
        return json_url;
    }

    public void setJson_url(String json_url) {
        this.json_url = json_url;
    }
}
