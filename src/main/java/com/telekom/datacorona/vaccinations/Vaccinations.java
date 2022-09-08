package com.telekom.datacorona.vaccinations;

import com.telekom.datacorona.region.Region;
import com.telekom.datacorona.vaccine.Vaccine;

import javax.persistence.*;

@Entity
public class Vaccinations {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "Vaccine.id", nullable = false)
    private Vaccine vaccine;
    @ManyToOne
    @JoinColumn(name = "Region.id", nullable = false)
    private Region region;
    private int dose1Count;
    private int dose2Count;
    private String updatedAt;
    private String publishedOn;

    public Vaccinations() {
    }

    public Vaccinations(String id) {
        this.id = id;
    }

    public Vaccinations(String id, Vaccine vaccine, Region region, int dose1Count, int dose2Count, String updatedAt, String publishedOn) {
        this.id = id;
        this.vaccine = vaccine;
        this.region = region;
        this.dose1Count = dose1Count;
        this.dose2Count = dose2Count;
        this.updatedAt = updatedAt;
        this.publishedOn = publishedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getDose1Count() {
        return dose1Count;
    }

    public void setDose1Count(int dose1Count) {
        this.dose1Count = dose1Count;
    }

    public int getDose2Count() {
        return dose2Count;
    }

    public void setDose2Count(int dose2Count) {
        this.dose2Count = dose2Count;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }
}
