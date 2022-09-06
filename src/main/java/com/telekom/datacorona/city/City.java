package com.telekom.datacorona.city;

import com.telekom.datacorona.district.District;
import com.telekom.datacorona.hospital.Hospital;

import javax.persistence.*;
import java.util.List;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "District.id", nullable = false)
    District district;
    private String code;
    private String title;
    @OneToMany(mappedBy = "id")
    private List<Hospital> hospitals;

    public City() {
    }

    public City(long id) {
        this.id = id;
    }

    public City(District district, String code, String title) {
        this.district = district;
        this.code = code;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
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

    @Override
    public String toString() {
        return "City{" +
                "district=" + district +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
