package com.telekom.datacorona.region;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Region {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String code;
    private String abbreviation;

    public Region() {
    }

    public Region(String title, String code, String abbreviation) {
        this.title = title;
        this.code = code;
        this.abbreviation = abbreviation;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}