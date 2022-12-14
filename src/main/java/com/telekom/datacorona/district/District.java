package com.telekom.datacorona.district;

import com.telekom.datacorona.city.City;
import com.telekom.datacorona.region.Region;

import javax.persistence.*;
import java.util.List;

@Entity
public class District {
    @Id
    private int id;
    @ManyToOne
    @JoinColumn(name = "Region.id", nullable = false)
    Region region;
    private String title;
    private String code;
    @OneToMany(mappedBy = "id")
    private List<City> cities;

    public District() {
    }

    public District(int id) {
        this.id = id;
    }

    public District(int id, Region region, String title, String code) {
        this.id = id;
        this.region = region;
        this.title = title;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Region getRegionId() {
        return region;
    }

    public void setRegionId(Region region) {
        this.region = region;
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
        return "District{" +
                "region=" + region +
                ", title='" + title + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
