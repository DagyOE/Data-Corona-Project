package com.telekom.datacorona.vaccinations;



import java.util.List;

public interface VaccinationsService {

    void addVaccinations(Vaccinations vaccinations);

    List<Vaccinations> getAllVaccinations();
}
