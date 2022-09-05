package com.telekom.datacorona.hospital;

import javax.persistence.*;

@Entity
public class Hospital {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "City.id", nullable = false)
    private long cityId;
    private String title;
    private String code;

    public Hospital() {

    }

    public Hospital(long cityId, String title, String code) {
        this.cityId = cityId;
        this.title = title;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}