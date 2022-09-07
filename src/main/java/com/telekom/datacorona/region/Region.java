package com.telekom.datacorona.region;

import com.telekom.datacorona.district.District;
import com.telekom.datacorona.regionVaccinations.RegionVaccinations;
import com.telekom.datacorona.vaccinations.Vaccinations;

import javax.persistence.*;
import java.util.List;

@Entity
public class Region {
    @Id
    private int id;
    private String title;
    private String code;
    private String abbreviation;
    @OneToMany(mappedBy = "id")
    private List<District> districts;
    @OneToMany(mappedBy = "id")
    private List<RegionVaccinations> regionVaccinations;
    @OneToMany(mappedBy = "id")
    private List<Vaccinations> vaccinations;

    public Region() {
    }

    public Region(int id) {
        this.id = id;
    }

    public Region(int id, String title, String code, String abbreviation) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Region{" +
                "title='" + title + '\'' +
                ", code='" + code + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}