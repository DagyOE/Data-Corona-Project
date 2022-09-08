package com.telekom.datacorona.regionHospitalPatients;

import com.telekom.datacorona.regionVaccinations.RegionVaccinationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hospital-patients/by-region")
public class RegionHospitalPatientsWebServiceREST {

    @Autowired
    RegionHospitalPatientsService regionHospitalPatientsService;

    @GetMapping
    public List<RegionHospitalPatients> regionHospitalPatients() {
        return regionHospitalPatientsService.getAllRegionHospitalPatients();
    }
}
