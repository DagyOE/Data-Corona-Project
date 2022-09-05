package com.telekom.datacorona.hospital;

import java.util.List;

public interface HospitalService {
    void addHospital(Hospital hospital);
    List<Hospital> getAllHospitals();
}
