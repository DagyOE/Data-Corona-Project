package com.telekom.datacorona.district;

import com.telekom.datacorona.city.City;
import com.telekom.datacorona.region.Region;

import javax.persistence.*;
import java.util.List;

@Entity
public class District {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "Region.id", nullable = false)
    Region region;
    private String title;
    private String code;
    @OneToMany(mappedBy = "id")
    private List<City> cities;

    public District() {
    }

    public District(Region region, String title, String code) {
        this.region = region;
        this.title = title;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
