package com.telekom.datacorona.regionVaccinations;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class RegionVaccinationController {

    RegionVaccinationsService regionVaccinationsService;

    @RequestMapping(value = "/hospital-patients/by-region/weekly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int getWeeklyData(@RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        List<RegionVaccinations> regionVaccinationsList = regionVaccinationsService.getAllRegionVaccinations();
        return 1;
    }
}
