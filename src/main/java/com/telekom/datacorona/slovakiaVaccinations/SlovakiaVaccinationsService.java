package com.telekom.datacorona.slovakiaVaccinations;

import java.util.List;

public interface SlovakiaVaccinationsService {

    void addSlovakiaVaccinations(SlovakiaVaccinations slovakiaVaccinations);

    List<SlovakiaVaccinations> getAllSlovakiaVaccinations();
}
