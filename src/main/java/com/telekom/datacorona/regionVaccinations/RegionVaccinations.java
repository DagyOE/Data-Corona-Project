package com.telekom.datacorona.regionVaccinations;

import com.telekom.datacorona.region.Region;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RegionVaccinations {

    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "Region.id", nullable = false)
    private Region region;
    private int dose1Count;
    private int dose2Count;
    private int dose1Sum;
    private int dose2Sum;
    private String updatedAt;
    private String publishedOn;

    public RegionVaccinations() {
    }

    public RegionVaccinations(String id, Region region, int dose1Count, int dose2Count, int dose1Sum, int dose2Sum, String updatedAt, String publishedOn) {
        this.id = id;
        this.region = region;
        this.dose1Count = dose1Count;
        this.dose2Count = dose2Count;
        this.dose1Sum = dose1Sum;
        this.dose2Sum = dose2Sum;
        this.updatedAt = updatedAt;
        this.publishedOn = publishedOn;
    }

    @Override
    public String toString() {
        return "RegionVaccinations{" +
                "id='" + id + '\'' +
                ", region=" + region +
                ", dose1Count=" + dose1Count +
                ", dose2Count=" + dose2Count +
                ", dose1Sum=" + dose1Sum +
                ", dose2Sum=" + dose2Sum +
                ", updatedAt='" + updatedAt + '\'' +
                ", publishedOn='" + publishedOn + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getDose1Sum() {
        return dose1Sum;
    }

    public void setDose1Sum(int dose1Sum) {
        this.dose1Sum = dose1Sum;
    }

    public int getDose2Sum() {
        return dose2Sum;
    }

    public void setDose2Sum(int dose2Sum) {
        this.dose2Sum = dose2Sum;
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
