package com.telekom.datacorona.regionHospitalPatients;

import java.util.List;

public interface RegionHospitalPatientsService {
    void addRegionHospitalPatients(RegionHospitalPatients regionHospitalPatients);
    List<RegionHospitalPatients> getAllRegionHospitalPatients();
}