package com.telekom.datacorona.slovakiaVaccinations;


import com.telekom.datacorona.slovakiaHospitalPatients.SlovakiaHospitalPatients;
import com.telekom.datacorona.slovakiaHospitalPatients.SlovakiaHospitalPatientsService;
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
public class SlovakiaVaccinationsController {

    @Autowired
    SlovakiaVaccinationsService slovakiaVaccinationsService;

    @RequestMapping(value = "/vaccinations/in-slovakia/weekly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SlovakiaVaccinations> getWeeklyData(@PathVariable String from, @PathVariable String to) {
        List<SlovakiaVaccinations> slovakiaHospitalPatientsList = slovakiaVaccinationsService.getDailyVaccinations(from, to);

        List<SlovakiaVaccinations> weeklySlovakiaHospitalPatientsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                int dose1Count = 0;
                int dose2Count = 0;

                int length = slovakiaHospitalPatientsList.size();
                for (int i = length - 1; i >= 0; i--) {
                    SlovakiaVaccinations sv = slovakiaHospitalPatientsList.get(i);

                    dose1Count += sv.getDose1Count();
                    dose2Count += sv.getDose2Count();



                    Date rvDate = createDate(sv.getPublishedOn());
                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        SlovakiaVaccinations newSV = new SlovakiaVaccinations(sv.getId(), dose1Count, dose2Count, sv.getDose1Sum(), sv.getDose2Sum(),
                                sv.getUpdatedAt(), sv.getPublishedOn());
                        weeklySlovakiaHospitalPatientsList.add(newSV);

                        dose1Count = 0;
                        dose2Count = 0;
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        Collections.reverse(weeklySlovakiaHospitalPatientsList);
        return weeklySlovakiaHospitalPatientsList;
    }

    @RequestMapping(value = "/vaccinations/in-slovakia/monthly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SlovakiaVaccinations> getMonthlyData(@PathVariable String from, @PathVariable String to) {
        List<SlovakiaVaccinations> slovakiaHospitalPatientsList = slovakiaVaccinationsService.getDailyVaccinations(from, to);

        List<SlovakiaVaccinations> monthlySlovakiaHospitalPatientsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                int dose1Count = 0;
                int dose2Count = 0;

                int length = slovakiaHospitalPatientsList.size();
                for (int i = length - 1; i >= 0; i--) {
                    SlovakiaVaccinations sv = slovakiaHospitalPatientsList.get(i);

                    dose1Count += sv.getDose1Count();
                    dose2Count += sv.getDose2Count();



                    Date rvDate = createDate(sv.getPublishedOn());
                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_MONTH) == getLastDayOfMonth(calendar.get(Calendar.MONTH))) {
                        SlovakiaVaccinations newSV = new SlovakiaVaccinations(sv.getId(), dose1Count, dose2Count, sv.getDose1Sum(), sv.getDose2Sum(),
                                sv.getUpdatedAt(), sv.getPublishedOn());
                        monthlySlovakiaHospitalPatientsList.add(newSV);

                        dose1Count = 0;
                        dose2Count = 0;
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
