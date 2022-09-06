package com.telekom.datacorona.regionVaccinations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RegionVaccinationsServiceJPA implements RegionVaccinationsService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addRegionVaccinations(RegionVaccinations regionVaccinations) {
        entityManager.persist(regionVaccinations);
    }

    @Override
    public List<RegionVaccinations> getAllRegionVaccinations() {
        return entityManager
                .createQuery("select rv from RegionVaccinations rv")
                .getResultList();
    }
}
