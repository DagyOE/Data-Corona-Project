package com.telekom.datacorona.regionVaccinations;

import java.util.Date;
import java.util.List;

public interface RegionVaccinationsService {

    void addRegionVaccinations(RegionVaccinations regionVaccinations);

    List<RegionVaccinations> getAllRegionVaccinations();

    List<RegionVaccinations> getCountRegionVaccinations(String from, String to);
    List<RegionVaccinations> getDailyRegionVaccinations(String from, String to);
    List<RegionVaccinations> getWeeklyRegionVaccinations(String from, String to);
}
