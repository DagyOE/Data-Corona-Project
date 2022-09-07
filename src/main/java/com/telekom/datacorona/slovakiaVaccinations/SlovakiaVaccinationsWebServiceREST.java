package com.telekom.datacorona.slovakiaVaccinations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vaccinations/in-slovakia")
public class SlovakiaVaccinationsWebServiceREST {

    @Autowired
    SlovakiaVaccinationsService slovakiaVaccinationsService;

    @PostMapping
    public void addSlovakiaVaccinations(SlovakiaVaccinations slovakiaVaccinations) {
        slovakiaVaccinationsService.addSlovakiaVaccinations(slovakiaVaccinations);
    }

    @GetMapping
    public List<SlovakiaVaccinations> getAllSlovakiaVaccinations() {
        return slovakiaVaccinationsService.getAllSlovakiaVaccinations();
    }
}
