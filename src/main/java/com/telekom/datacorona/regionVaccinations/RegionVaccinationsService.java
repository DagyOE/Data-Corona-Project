package com.telekom.datacorona.regionVaccinations;

import java.util.List;

public interface RegionVaccinationsService {

    void addRegionVaccinations(RegionVaccinations regionVaccinations);

    List<RegionVaccinations> getAllRegionVaccinations();
}
