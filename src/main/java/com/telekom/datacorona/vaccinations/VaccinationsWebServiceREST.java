package com.telekom.datacorona.vaccinations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vaccinations")
public class VaccinationsWebServiceREST {

    @Autowired
    VaccinationsService vaccinationsService;

    @PostMapping
    public void addVaccinations(Vaccinations vaccinations) {
       vaccinationsService.addVaccinations(vaccinations);
    }

    @GetMapping
    public List<Vaccinations> getAllVaccinations() {
        return vaccinationsService.getAllVaccinations();
    }
}
