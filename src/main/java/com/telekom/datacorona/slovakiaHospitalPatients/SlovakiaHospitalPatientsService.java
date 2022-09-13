package com.telekom.datacorona.slovakiaHospitalPatients;

import com.telekom.datacorona.regionVaccinations.RegionVaccinations;

import java.util.List;

public interface SlovakiaHospitalPatientsService {

    void addSlovakiaHospitalPatients(SlovakiaHospitalPatients slovakiaHospitalPatients);

    List<SlovakiaHospitalPatients> getAllSlovakiaHospitalPatients();

    List<SlovakiaHospitalPatients> getDailyHospitalPatients(String from, String to);
}
