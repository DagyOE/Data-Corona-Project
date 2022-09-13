package com.telekom.datacorona.regionVaccinations;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
}

