package com.telekom.datacorona.regionHospitalPatients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/api")
public class RegionHospitalPatientsController {

    @Autowired
    RegionHospitalPatientsService regionHospitalPatientsService;

    @RequestMapping(value = "/hospital-patients/by-region/weekly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RegionHospitalPatients> getWeeklyData(@PathVariable String from, @PathVariable String to) {
        List<RegionHospitalPatients> regionRegionHospitalPatientsList = regionHospitalPatientsService.getDailyRegionHospitalPatients(from, to);

        List<RegionHospitalPatients> weeklyRegionHospitalPatientsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                int[] ventilatedCovid = new int[8];
                int[] nonCovid = new int[8];
                int[] confirmedCovid = new int[8];
                int[] suspectedCovid = new int[8];

                int length = regionRegionHospitalPatientsList.size();
                for (int i = length - 1; i >= 0; i--) {
                    RegionHospitalPatients rhp = regionRegionHospitalPatientsList.get(i);
                    int region = rhp.getRegion().getId();

                    ventilatedCovid[region - 1] += rhp.getVentilatedCovid();
                    nonCovid[region - 1] += rhp.getNonCovid();
                    confirmedCovid[region - 1] += rhp.getConfirmedCovid();
                    suspectedCovid[region - 1] += rhp.getSuspectedCovid();


                    Date rvDate = createDate(rhp.getPublishedOn());
                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        RegionHospitalPatients newRHP = new RegionHospitalPatients(rhp.getId(), rhp.getOldestReportedAt(), rhp.getNewestReportedAt(),
                                rhp.getRegion(), ventilatedCovid[region - 1], nonCovid[region - 1], confirmedCovid[region - 1],
                                suspectedCovid[region - 1], rhp.getPublishedOn(), rhp.getUpdatedAt());
                        weeklyRegionHospitalPatientsList.add(newRHP);

                        ventilatedCovid[region - 1] = 0;
                        nonCovid[region - 1] = 0;
                        confirmedCovid[region - 1] = 0;
                        suspectedCovid[region - 1] = 0;
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return weeklyRegionHospitalPatientsList;
    }

    @RequestMapping(value = "/hospital-patients/by-region/monthly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RegionHospitalPatients> getMonthlyData(@PathVariable String from, @PathVariable String to) {
        List<RegionHospitalPatients> regionRegionHospitalPatientsList = regionHospitalPatientsService.getDailyRegionHospitalPatients(from, to);

        List<RegionHospitalPatients> weeklyRegionHospitalPatientsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                int[] ventilatedCovid = new int[8];
                int[] nonCovid = new int[8];
                int[] confirmedCovid = new int[8];
                int[] suspectedCovid = new int[8];

                int length = regionRegionHospitalPatientsList.size();
                for (int i = length - 1; i >= 0; i--) {
                    RegionHospitalPatients rhp = regionRegionHospitalPatientsList.get(i);
                    int region = rhp.getRegion().getId();

                    ventilatedCovid[region - 1] += rhp.getVentilatedCovid();
                    nonCovid[region - 1] += rhp.getNonCovid();
                    confirmedCovid[region - 1] += rhp.getConfirmedCovid();
                    suspectedCovid[region - 1] += rhp.getSuspectedCovid();


                    Date rvDate = createDate(rhp.getPublishedOn());
                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_MONTH) == getLastDayOfMonth(calendar.get(Calendar.MONTH))) {
                        RegionHospitalPatients newRHP = new RegionHospitalPatients(rhp.getId(), rhp.getOldestReportedAt(), rhp.getNewestReportedAt(),
                                rhp.getRegion(), ventilatedCovid[region - 1], nonCovid[region - 1], confirmedCovid[region - 1],
                                suspectedCovid[region - 1], rhp.getPublishedOn(), rhp.getUpdatedAt());
                        weeklyRegionHospitalPatientsList.add(newRHP);

                        ventilatedCovid[region - 1] = 0;
                        nonCovid[region - 1] = 0;
                        confirmedCovid[region - 1] = 0;
                        suspectedCovid[region - 1] = 0;
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return weeklyRegionHospitalPatientsList;
    }

    private Date createDate(String date_string) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(date_string);
        return date;
    }

    private int getLastDayOfMonth(int month) {
        if (month == 1)
            return 28;
        if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11)
            return 31;
        return 30;
    }
}
