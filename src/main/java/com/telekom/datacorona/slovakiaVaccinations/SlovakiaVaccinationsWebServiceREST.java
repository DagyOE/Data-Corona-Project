package com.telekom.datacorona.slovakiaVaccinations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/vaccinations/in-slovakia")
public class SlovakiaVaccinationsWebServiceREST {

    @Autowired
    SlovakiaVaccinationsService slovakiaVaccinationsService;

    @GetMapping
    public List<SlovakiaVaccinations> getAllSlovakiaVaccinations() {
        return slovakiaVaccinationsService.getAllSlovakiaVaccinations();
    }
}
