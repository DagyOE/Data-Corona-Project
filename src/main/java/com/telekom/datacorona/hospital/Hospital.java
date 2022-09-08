package com.telekom.datacorona.hospital;

import com.telekom.datacorona.city.City;

import javax.persistence.*;

@Entity
public class Hospital {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(name = "City.id", nullable = false)
    City city;
    private String title;
    private String code;

    public Hospital() {
    }

    public Hospital(int id) {
        this.id = id;
    }

    public Hospital(int id, City city, String title, String code) {
        this.id = id;
        this.city = city;
        this.title = title;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Hospital{" +
                "city=" + city +
                ", title='" + title + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}