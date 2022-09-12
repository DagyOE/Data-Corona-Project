package com.telekom.datacorona.vaccinations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/vaccinations")
public class VaccinationsWebServiceREST {
    @Autowired
    VaccinationsService vaccinationsService;

    @GetMapping
    public List<Vaccinations> getAllVaccinations() {
        return vaccinationsService.getAllVaccinations();
    }
}
