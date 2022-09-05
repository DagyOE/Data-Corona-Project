package com.telekom.datacorona.regionVaccinations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regionVaccinations")
public class RegionVaccinationsWebServiceREST {

    @Autowired
    RegionVaccinationsService regionVaccinationsService;

    @PostMapping
    public void addRegionVaccinations(RegionVaccinations regionVaccinations) {
        regionVaccinationsService.addRegionVaccinations(regionVaccinations);
    }

    @GetMapping
    public List<RegionVaccinations> getAllRegionVaccinations() {
        return regionVaccinationsService.getAllRegionVaccinations();
    }
}
