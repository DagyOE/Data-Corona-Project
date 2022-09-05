package com.telekom.datacorona.district;

import com.telekom.datacorona.city.City;

import javax.persistence.*;
import java.util.List;

@Entity
public class District {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "Region.id", nullable = false)
    private long regionId;
    private String title;
    private String code;
    @OneToMany(mappedBy = "id")
    private List<City> cities;

    public District() {
    }

    public District(long regionId, String title, String code) {
        this.regionId = regionId;
        this.title = title;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
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
