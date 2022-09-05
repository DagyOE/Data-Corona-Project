package com.telekom.datacorona.hospital;

import com.telekom.datacorona.city.City;

import javax.persistence.*;

@Entity
public class Hospital {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "City.id", nullable = false)
    City city;
    private String title;
    private String code;

    public Hospital() {

    }

    public Hospital(City city, String title, String code) {
        this.city = city;
        this.title = title;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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