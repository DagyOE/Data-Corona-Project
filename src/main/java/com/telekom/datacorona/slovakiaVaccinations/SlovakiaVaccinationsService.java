package com.telekom.datacorona.slovakiaVaccinations;

import com.telekom.datacorona.regionVaccinations.RegionVaccinations;

import java.util.List;

public interface SlovakiaVaccinationsService {

    void addSlovakiaVaccinations(SlovakiaVaccinations slovakiaVaccinations);

    List<SlovakiaVaccinations> getAllSlovakiaVaccinations();

    List<RegionVaccinations> getDailyVaccinations(String from, String to);
}
