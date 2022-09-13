package com.telekom.datacorona.regionHospitalPatients;

import com.telekom.datacorona.regionVaccinations.RegionVaccinations;

import java.util.List;

public interface RegionHospitalPatientsService {
    void addRegionHospitalPatients(RegionHospitalPatients regionHospitalPatients);
    List<RegionHospitalPatients> getAllRegionHospitalPatients();
    List<RegionHospitalPatients> getCountRegionHospitalPatients(String from, String to);

    List<RegionHospitalPatients> getDailyRegionHospitalPatients(String from, String to);
}
