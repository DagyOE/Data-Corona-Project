package com.telekom.datacorona.regionHospitalPatients;

import com.telekom.datacorona.regionVaccinations.RegionVaccinations;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/hospital-patients/by-region")
public class RegionHospitalPatientsWebServiceREST {

    @Autowired
    RegionHospitalPatientsService regionHospitalPatientsService;

    @GetMapping
    public List<RegionHospitalPatients> regionHospitalPatients() {
        return regionHospitalPatientsService.getAllRegionHospitalPatients();
    }

    @GetMapping("/{from}/{to}")
    public List<RegionHospitalPatients> getCountRegionHospitalPatients(@PathVariable String from, @PathVariable String to) {
        return regionHospitalPatientsService.getCountRegionHospitalPatients(from, to);
    }

    @GetMapping("/daily/{from}/{to}")
    public List<RegionHospitalPatients> getDailyRegionHospitalPatients(@PathVariable String from, @PathVariable String to) {
        return regionHospitalPatientsService.getDailyRegionHospitalPatients(from, to);
    }
}
