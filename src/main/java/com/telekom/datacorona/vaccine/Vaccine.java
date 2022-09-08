package com.telekom.datacorona.vaccine;

import com.telekom.datacorona.vaccinations.Vaccinations;

import javax.persistence.*;
import java.util.List;

@Entity
public class Vaccine {
    @Id
    private int id;
    private String title;
    private String manufacturer;
    @OneToMany(mappedBy = "id")
    private List<Vaccinations> vaccinations;

    public Vaccine() {
    }

    public Vaccine(int id) {
        this.id = id;
    }

    public Vaccine(int id, String title, String manufacturer) {
        this.id = id;
        this.title = title;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
