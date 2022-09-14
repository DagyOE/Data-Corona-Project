package com.telekom.datacorona.slovakiaHospitalPatients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api")
public class SlovakiaHospitalPatientsController {

    @Autowired
    SlovakiaHospitalPatientsService slovakiaHospitalPatientsService;

    @RequestMapping(value = "/hospital-patients/in-slovakia/weekly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SlovakiaHospitalPatients> getWeeklyData(@PathVariable String from, @PathVariable String to) {
        List<SlovakiaHospitalPatients> slovakiaHospitalPatientsList = slovakiaHospitalPatientsService.getDailyHospitalPatients(from, to);

        List<SlovakiaHospitalPatients> weeklySlovakiaHospitalPatientsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                int ventilatedCovid = 0;
                int nonCovid = 0;
                int confirmedCovid = 0;
                int suspectedCovid = 0;

                int length = slovakiaHospitalPatientsList.size();
                for (int i = length - 1; i >= 0; i--) {
                    SlovakiaHospitalPatients shp = slovakiaHospitalPatientsList.get(i);

                    ventilatedCovid += shp.getVentilatedCovid();
                    nonCovid += shp.getNonCovid();
                    confirmedCovid += shp.getConfirmedCovid();
                    suspectedCovid += shp.getSuspectedCovid();


                    Date rvDate = createDate(shp.getPublishedOn());
                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        SlovakiaHospitalPatients newSHP = new SlovakiaHospitalPatients(shp.getId(), shp.getOldestReportedAt(), shp.getNewestReportedAt(),
                                ventilatedCovid, nonCovid, confirmedCovid, suspectedCovid, shp.getPublishedOn(), shp.getUpdatedAt());
                        weeklySlovakiaHospitalPatientsList.add(newSHP);

                        ventilatedCovid = 0;
                        nonCovid = 0;
                        confirmedCovid = 0;
                        suspectedCovid = 0;
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        Collections.reverse(weeklySlovakiaHospitalPatientsList);
        return weeklySlovakiaHospitalPatientsList;
    }

    @RequestMapping(value = "/hospital-patients/in-slovakia/monthly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SlovakiaHospitalPatients> getMonthlyData(@PathVariable String from, @PathVariable String to) {
        List<SlovakiaHospitalPatients> slovakiaHospitalPatientsList = slovakiaHospitalPatientsService.getDailyHospitalPatients(from, to);

        List<SlovakiaHospitalPatients> monthlySlovakiaHospitalPatientsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                int ventilatedCovid = 0;
                int nonCovid = 0;
                int confirmedCovid = 0;
                int suspectedCovid = 0;

                int length = slovakiaHospitalPatientsList.size();
                for (int i = length - 1; i >= 0; i--) {
                    SlovakiaHospitalPatients shp = slovakiaHospitalPatientsList.get(i);

                    ventilatedCovid += shp.getVentilatedCovid();
                    nonCovid += shp.getNonCovid();
                    confirmedCovid += shp.getConfirmedCovid();
                    suspectedCovid += shp.getSuspectedCovid();


                    Date rvDate = createDate(shp.getPublishedOn());
                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_MONTH) == getLastDayOfMonth(calendar.get(Calendar.MONTH))) {
                        SlovakiaHospitalPatients newSHP = new SlovakiaHospitalPatients(shp.getId(), shp.getOldestReportedAt(), shp.getNewestReportedAt(),
                                ventilatedCovid, nonCovid, confirmedCovid, suspectedCovid, shp.getPublishedOn(), shp.getUpdatedAt());
                        monthlySlovakiaHospitalPatientsList.add(newSHP);

                        ventilatedCovid = 0;
                        nonCovid = 0;
                        confirmedCovid = 0;
                        suspectedCovid = 0;
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        Collections.reverse(monthlySlovakiaHospitalPatientsList);
        return monthlySlovakiaHospitalPatientsList;
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
