package com.telekom.datacorona.regionVaccinations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/api")
public class RegionVaccinationController {

    @Autowired
    RegionVaccinationsService regionVaccinationsService;

    @RequestMapping(value = "/vaccinations/by-region/weekly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RegionVaccinations> getWeeklyData(@PathVariable String from, @PathVariable String to) {
        List<RegionVaccinations> regionVaccinationsList = regionVaccinationsService.getAllRegionVaccinations();

        List<RegionVaccinations> weeklyRegionVaccinationsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Date fromDate = createDate(from);
                Date toDate = createDate(to);

                Calendar calendar = Calendar.getInstance();
                int[] regionsDose1Count = new int[8];
                int[] regionsDose2Count = new int[8];
                for (RegionVaccinations rv : regionVaccinationsList) {
                    Date rvDate = createDate(rv.getPublishedOn());

                    if (!rvDate.before(fromDate) && !rvDate.after(toDate)) {
                        int region = rv.getRegion().getId();

                        regionsDose1Count[region - 1] += rv.getDose1Count();
                        regionsDose2Count[region - 1] += rv.getDose2Count();

                        calendar.setTime(rvDate);
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            RegionVaccinations newRV = new RegionVaccinations(rv.getId(), rv.getRegion(), regionsDose1Count[region - 1], regionsDose2Count[region - 1],
                                    rv.getDose1Sum(), rv.getDose2Sum(), rv.getUpdatedAt(), rv.getPublishedOn());
                            weeklyRegionVaccinationsList.add(newRV);

                            regionsDose1Count[region - 1] = 0;
                            regionsDose2Count[region - 1] = 0;
                        }
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return weeklyRegionVaccinationsList;
    }

    @RequestMapping(value = "/vaccinations/by-region/monthly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RegionVaccinations> getMonthlyData(@PathVariable String from, @PathVariable String to) {
        List<RegionVaccinations> regionVaccinationsList = regionVaccinationsService.getAllRegionVaccinations();

        List<RegionVaccinations> monthlyRegionVaccinationsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Date fromDate = createDate(from);
                Date toDate = createDate(to);

                Calendar calendar = Calendar.getInstance();
                int[] regionsDose1Count = new int[8];
                int[] regionsDose2Count = new int[8];
                for (RegionVaccinations rv : regionVaccinationsList) {
                    Date rvDate = createDate(rv.getPublishedOn());

                    if (!rvDate.before(fromDate) && !rvDate.after(toDate)) {
                        int region = rv.getRegion().getId();

                        regionsDose1Count[region - 1] += rv.getDose1Count();
                        regionsDose2Count[region - 1] += rv.getDose2Count();

                        calendar.setTime(rvDate);
                        if (calendar.get(Calendar.DAY_OF_MONTH) == getLastDayOfMonth(calendar.get(Calendar.MONTH))) {
                            RegionVaccinations newRV = new RegionVaccinations(rv.getId(), rv.getRegion(), regionsDose1Count[region - 1], regionsDose2Count[region - 1],
                                    rv.getDose1Sum(), rv.getDose2Sum(), rv.getUpdatedAt(), rv.getPublishedOn());
                            monthlyRegionVaccinationsList.add(newRV);

                            regionsDose1Count[region - 1] = 0;
                            regionsDose2Count[region - 1] = 0;
                        }
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return monthlyRegionVaccinationsList;
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
