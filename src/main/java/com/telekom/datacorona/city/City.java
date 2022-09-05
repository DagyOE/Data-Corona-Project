package com.telekom.datacorona.city;

import com.telekom.datacorona.hospital.Hospital;

import javax.persistence.*;
import java.util.List;

@Entity
public class City {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "District.id", nullable = false)
    private long districtId;
    private String code;
    private String title;
    @OneToMany(mappedBy = "id")
    private List<Hospital> hospitals;

    public City() {
    }

    public City(long districtId, String code, String title) {
        this.districtId = districtId;
        this.code = code;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
