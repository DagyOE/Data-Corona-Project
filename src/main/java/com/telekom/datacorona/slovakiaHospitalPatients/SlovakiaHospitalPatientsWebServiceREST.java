package com.telekom.datacorona.slovakiaHospitalPatients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hospital-patients/in-slovakia")
public class SlovakiaHospitalPatientsWebServiceREST {

    @Autowired
    SlovakiaHospitalPatientsService slovakiaHospitalPatients;

    @GetMapping
    public List<SlovakiaHospitalPatients> getAllSlovakiaHospitalPatients() {
        return slovakiaHospitalPatients.getAllSlovakiaHospitalPatients();
    }
}
