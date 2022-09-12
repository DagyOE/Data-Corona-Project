package com.telekom.datacorona.district;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/districts")
public class DistrictWebServiceREST {
    @Autowired
    DistrictService districtService;

    @GetMapping
    public List<District> getAllDistricts() {
        return districtService.getAllDistricts();
    }
}
