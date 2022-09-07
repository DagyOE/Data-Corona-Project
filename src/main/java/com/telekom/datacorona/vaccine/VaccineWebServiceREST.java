package com.telekom.datacorona.vaccine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vaccines")
public class VaccineWebServiceREST {
    @Autowired
    VaccineService vaccineService;

    @PostMapping
    public void addVaccine(Vaccine vaccine) {
        vaccineService.addVaccine(vaccine);
    }

    @GetMapping
    public List<Vaccine> getAllVaccines() {
        return vaccineService.getAllVaccines();
    }
}
