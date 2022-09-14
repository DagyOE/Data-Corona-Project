package com.telekom.datacorona.regionVaccinations;

import com.telekom.datacorona.region.RegionService;
import com.telekom.datacorona.regionHospitalPatients.RegionHospitalPatients;
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
    @Autowired
    RegionService regionService;

    @RequestMapping(value = "/vaccinations/by-region/weekly/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RegionVaccinations> getWeeklyData(@PathVariable String from, @PathVariable String to) {
        List<RegionVaccinations> regionVaccinationsList = regionVaccinationsService.getDailyRegionVaccinations(from, to);

        List<RegionVaccinations> weeklyRegionVaccinationsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                RegionVaccinations[] regionVaccinations = new RegionVaccinations[8];
                for (int region = 0; region < 8; region++) {
                    regionVaccinations[region] = new RegionVaccinations();
                    regionVaccinations[region].setRegion(regionService.getAllRegions().get(region));
                }

                String publishedOn = "";
                boolean newWeek = false;
                int length = regionVaccinationsList.size();
                Date rvDate = new Date();
                for (int i = length - 1; i >= 0; i--) {
                    RegionVaccinations rv = regionVaccinationsList.get(i);
                    rvDate = createDate(rv.getPublishedOn());

                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && newWeek) {
                        addRegionVaccinations(weeklyRegionVaccinationsList, regionVaccinations, publishedOn);
                        newWeek = false;
                    } else if (!newWeek && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                        newWeek = true;
                    }

                    int region = rv.getRegion().getId();

                    regionVaccinations[region - 1].setId(rv.getId());
                    regionVaccinations[region - 1].setDose1Count(regionVaccinations[region - 1].getDose1Count() + rv.getDose1Count());
                    regionVaccinations[region - 1].setDose2Count(regionVaccinations[region - 1].getDose2Count() + rv.getDose2Count());
                    regionVaccinations[region - 1].setDose1Sum(rv.getDose1Sum());
                    regionVaccinations[region - 1].setDose2Sum(rv.getDose2Sum());
                    regionVaccinations[region - 1].setUpdatedAt(rv.getUpdatedAt());
                    publishedOn = rv.getPublishedOn();
                }
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    addRegionVaccinations(weeklyRegionVaccinationsList, regionVaccinations, publishedOn);
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
        List<RegionVaccinations> regionVaccinationsList = regionVaccinationsService.getDailyRegionVaccinations(from, to);

        List<RegionVaccinations> monthlyRegionVaccinationsList = new ArrayList<>();

        if (from != null && to != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                RegionVaccinations[] regionVaccinations = new RegionVaccinations[8];
                for (int region = 0; region < 8; region++) {
                    regionVaccinations[region] = new RegionVaccinations();
                    regionVaccinations[region].setRegion(regionService.getAllRegions().get(region));
                }

                String publishedOn = "";
                boolean newMonth = false;
                int length = regionVaccinationsList.size();
                Date rvDate = new Date();
                for (int i = length - 1; i >= 0; i--) {
                    RegionVaccinations rv = regionVaccinationsList.get(i);
                    rvDate = createDate(rv.getPublishedOn());

                    calendar.setTime(rvDate);
                    if (calendar.get(Calendar.DAY_OF_MONTH) == 1 && newMonth) {
                        addRegionVaccinations(monthlyRegionVaccinationsList, regionVaccinations, publishedOn);
                        newMonth = false;
                    } else if (!newMonth && calendar.get(Calendar.DAY_OF_MONTH) != 1) {
                        newMonth = true;
                    }

                    int region = rv.getRegion().getId();

                    regionVaccinations[region - 1].setId(rv.getId());
                    regionVaccinations[region - 1].setDose1Count(regionVaccinations[region - 1].getDose1Count() + rv.getDose1Count());
                    regionVaccinations[region - 1].setDose2Count(regionVaccinations[region - 1].getDose2Count() + rv.getDose2Count());
                    regionVaccinations[region - 1].setDose1Sum(rv.getDose1Sum());
                    regionVaccinations[region - 1].setDose2Sum(rv.getDose2Sum());
                    regionVaccinations[region - 1].setUpdatedAt(rv.getUpdatedAt());
                    publishedOn = rv.getPublishedOn();
                }
                if (calendar.get(Calendar.DAY_OF_MONTH) == getLastDayOfMonth(calendar.get(Calendar.MONTH))) {
                    addRegionVaccinations(monthlyRegionVaccinationsList, regionVaccinations, publishedOn);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return monthlyRegionVaccinationsList;
    }

    private void addRegionVaccinations(List<RegionVaccinations> rvList, RegionVaccinations[] regionVaccinations, String publishedOn) {
        for (int region = 0; region < 8; region++) {
            rvList.add(new RegionVaccinations(regionVaccinations[region].getId(), regionVaccinations[region].getRegion(),
                    regionVaccinations[region].getDose1Count(), regionVaccinations[region].getDose2Count(), regionVaccinations[region].getDose1Sum(),
                    regionVaccinations[region].getDose2Sum(), regionVaccinations[region].getUpdatedAt(), publishedOn));
            regionVaccinations[region] = new RegionVaccinations();
            regionVaccinations[region].setRegion(regionService.getAllRegions().get(region));
        }
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
