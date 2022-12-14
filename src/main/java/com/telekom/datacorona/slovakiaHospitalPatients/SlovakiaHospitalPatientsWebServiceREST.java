package com.telekom.datacorona.slovakiaHospitalPatients;

import com.telekom.datacorona.regionVaccinations.RegionVaccinations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/hospital-patients/in-slovakia")
public class SlovakiaHospitalPatientsWebServiceREST {

    @Autowired
    SlovakiaHospitalPatientsService slovakiaHospitalPatients;

    @GetMapping
    public List<SlovakiaHospitalPatients> getAllSlovakiaHospitalPatients() {
        return slovakiaHospitalPatients.getAllSlovakiaHospitalPatients();
    }

    @GetMapping("/daily/{from}/{to}")
    public List<SlovakiaHospitalPatients> getDailyRegionHospitalPatients(@PathVariable String from, @PathVariable String to) {
        return slovakiaHospitalPatients.getDailyHospitalPatients(from, to);
    }
}
