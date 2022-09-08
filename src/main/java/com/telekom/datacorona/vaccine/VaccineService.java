package com.telekom.datacorona.vaccine;

import java.util.List;

public interface VaccineService {
    void addVaccine(Vaccine vaccine);
    List<Vaccine> getAllVaccines();
}
