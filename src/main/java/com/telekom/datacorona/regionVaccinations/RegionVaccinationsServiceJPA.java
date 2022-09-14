package com.telekom.datacorona.regionVaccinations;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Transactional
public class RegionVaccinationsServiceJPA implements RegionVaccinationsService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addRegionVaccinations(RegionVaccinations regionVaccinations) {
        try {
            entityManager
                    .createQuery("select rv from RegionVaccinations rv where rv.id= :id")
                    .setParameter("id", regionVaccinations.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException iae) {
            entityManager.persist(regionVaccinations);
        } catch (NoResultException nre) {
            entityManager.persist(regionVaccinations);
        }
    }

    @Override
    public List<RegionVaccinations> getAllRegionVaccinations() {
        return entityManager
                .createQuery("select rv from RegionVaccinations rv")
                .getResultList();
    }

    @Override
    public List<RegionVaccinations> getCountRegionVaccinations(String from, String to) {
        return entityManager
                .createQuery("select rv.region.title, sum(rv.dose1Count) as dose1Count, sum(rv.dose2Count) AS dose2Count from RegionVaccinations rv where rv.publishedOn between :from and :to group by rv.region.title")
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<RegionVaccinations> getDailyRegionVaccinations(String from, String to) {
        return entityManager
                .createQuery("select rv from RegionVaccinations rv where rv.publishedOn between :from and :to")
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<RegionVaccinations> getWeeklyRegionVaccinations(String from, String to) {
        List<RegionVaccinations> regionVaccinationsList = getAllRegionVaccinations();

        List<RegionVaccinations> weeklyRegionVaccinationsList = new ArrayList<>();

        try {
            Date fromDate = createDate(from);
            Date toDate = createDate(to);

            Calendar calendar = Calendar.getInstance();
            int[] regionsDose1Count = new int[8];
            int[] regionsDose2Count = new int[8];
            for (RegionVaccinations rv: regionVaccinationsList) {
                Date rvDate = createDate(rv.getPublishedOn());

                if (!rvDate.before(fromDate) && !rvDate.after(toDate)) {
                    int region = rv.getRegion().getId();

                    regionsDose1Count[region - 1] += rv.getDose1Count();
                    regionsDose2Count[region - 1] += rv.getDose2Count();

                    calendar.setTime(rvDate);
                    if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
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

        return weeklyRegionVaccinationsList;
    }

    private Date createDate(String date_string) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(date_string);
        return date;
    }
}

