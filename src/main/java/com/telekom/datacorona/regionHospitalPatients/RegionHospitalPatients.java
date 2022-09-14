package com.telekom.datacorona.regionHospitalPatients;

import com.telekom.datacorona.region.Region;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RegionHospitalPatients {

    @Id
    private String id;
    private String oldestReportedAt;
    private String newestReportedAt;
    @ManyToOne
    @JoinColumn(name = "Region.id", nullable = false)
    private Region region;
    private int ventilatedCovid;
    private int nonCovid;
    private int confirmedCovid;
    private int suspectedCovid;
    private String publishedOn;
    private String updatedAt;

    public RegionHospitalPatients() {
    }

    public RegionHospitalPatients(String id) {
        this.id = id;
    }

    public RegionHospitalPatients(String id, String oldestReportedAt, String newestReportedAt, Region region, int ventilatedCovid, int nonCovid, int confirmedCovid, int suspectedCovid, String publishedOn, String updatedAt) {
        this.id = id;
        this.oldestReportedAt = oldestReportedAt;
        this.newestReportedAt = newestReportedAt;
        this.region = region;
        this.ventilatedCovid = ventilatedCovid;
        this.nonCovid = nonCovid;
        this.confirmedCovid = confirmedCovid;
        this.suspectedCovid = suspectedCovid;
        this.publishedOn = publishedOn;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "RegionHospitalPatients{" +
                "id='" + id + '\'' +
                ", oldestReportedAt='" + oldestReportedAt + '\'' +
                ", newestReportedAt='" + newestReportedAt + '\'' +
                ", region=" + region +
                ", ventilatedCovid=" + ventilatedCovid +
                ", nonCovid=" + nonCovid +
                ", confirmedCovid=" + confirmedCovid +
                ", suspectedCovid=" + suspectedCovid +
                ", publishedOn='" + publishedOn + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOldestReportedAt() {
        return oldestReportedAt;
    }

    public void setOldestReportedAt(String oldestReportedAt) {
        this.oldestReportedAt = oldestReportedAt;
    }

    public String getNewestReportedAt() {
        return newestReportedAt;
    }

    public void setNewestReportedAt(String newestReportedAt) {
        this.newestReportedAt = newestReportedAt;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getVentilatedCovid() {
        return ventilatedCovid;
    }

    public void setVentilatedCovid(int ventilatedCovid) {
        this.ventilatedCovid = ventilatedCovid;
    }

    public int getNonCovid() {
        return nonCovid;
    }

    public void setNonCovid(int nonCovid) {
        this.nonCovid = nonCovid;
    }

    public int getConfirmedCovid() {
        return confirmedCovid;
    }

    public void setConfirmedCovid(int confirmedCovid) {
        this.confirmedCovid = confirmedCovid;
    }

    public int getSuspectedCovid() {
        return suspectedCovid;
    }

    public void setSuspectedCovid(int suspectedCovid) {
        this.suspectedCovid = suspectedCovid;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
