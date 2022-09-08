package com.telekom.datacorona.regionVaccinations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vaccinations/by-region")
public class RegionVaccinationsWebServiceREST {

    @Autowired
    RegionVaccinationsService regionVaccinationsService;

    @GetMapping
    public List<RegionVaccinations> getAllRegionVaccinations() {
        return regionVaccinationsService.getAllRegionVaccinations();
    }
}
