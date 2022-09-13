package com.telekom.datacorona.regionVaccinations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/vaccinations/by-region")
public class RegionVaccinationsWebServiceREST {

    @Autowired
    RegionVaccinationsService regionVaccinationsService;

    @GetMapping
    public List<RegionVaccinations> getAllRegionVaccinations() {
        return regionVaccinationsService.getAllRegionVaccinations();
    }

    @GetMapping("/{from}/{to}")
    public List<RegionVaccinations> getCountRegionVaccinations(@PathVariable String from,@PathVariable String to) {
        return regionVaccinationsService.getCountRegionVaccinations(from, to);
    }

    @GetMapping("/daily/{from}/{to}")
    public List<RegionVaccinations> getDailyRegionVaccinations(@PathVariable String from, @PathVariable String to) {
        return regionVaccinationsService.getDailyRegionVaccinations(from, to);
    }
}
