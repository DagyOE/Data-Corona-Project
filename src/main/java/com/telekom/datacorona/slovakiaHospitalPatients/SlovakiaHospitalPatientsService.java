package com.telekom.datacorona.slovakiaHospitalPatients;

import java.util.List;

public interface SlovakiaHospitalPatientsService {

    void addSlovakiaHospitalPatients(SlovakiaHospitalPatients slovakiaHospitalPatients);

    List<SlovakiaHospitalPatients> getAllSlovakiaHospitalPatients();
}
