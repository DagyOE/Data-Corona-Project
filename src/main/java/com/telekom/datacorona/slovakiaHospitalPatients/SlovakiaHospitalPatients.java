package com.telekom.datacorona.slovakiaHospitalPatients;

import com.telekom.datacorona.region.Region;

import javax.persistence.*;
import java.util.List;

@Entity
public class SlovakiaHospitalPatients {

    @Id
    private String id;
    private String oldestReportedAt;
    private String newestReportedAt;
    private int ventilatedCovid;
    private int nonCovid;
    private int confirmedCovid;
    private int suspectedCovid;
    private String publishedOn;
    private String updatedAt;

    public SlovakiaHospitalPatients() {
    }

    public SlovakiaHospitalPatients(String id) {
        this.id = id;
    }

    public SlovakiaHospitalPatients(String id, String oldestReportedAt, String newestReportedAt, int ventilatedCovid, int nonCovid, int confirmedCovid, int suspectedCovid, String publishedOn, String updatedAt) {
        this.id = id;
        this.oldestReportedAt = oldestReportedAt;
        this.newestReportedAt = newestReportedAt;
        this.ventilatedCovid = ventilatedCovid;
        this.nonCovid = nonCovid;
        this.confirmedCovid = confirmedCovid;
        this.suspectedCovid = suspectedCovid;
        this.publishedOn = publishedOn;
        this.updatedAt = updatedAt;
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
