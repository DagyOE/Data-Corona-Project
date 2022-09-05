package com.telekom.datacorona.region;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RegionServiceJPA implements RegionService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addRegion(Region region) {
        entityManager.persist(region);
    }

    @Override
    public List<Region> getAllRegions() {
        return entityManager
                .createQuery("select r from Region r")
                .getResultList();
    }
}
