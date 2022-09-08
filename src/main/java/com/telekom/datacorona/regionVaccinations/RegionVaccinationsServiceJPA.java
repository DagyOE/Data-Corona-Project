package com.telekom.datacorona.regionVaccinations;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
                    .getResultList();
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
}
