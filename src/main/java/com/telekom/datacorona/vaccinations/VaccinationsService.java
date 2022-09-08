package com.telekom.datacorona.vaccinations;

import java.util.List;

public interface VaccinationsService {
    void addVaccination(Vaccinations vaccinations);
    List<Vaccinations> getAllVaccinations();
}
