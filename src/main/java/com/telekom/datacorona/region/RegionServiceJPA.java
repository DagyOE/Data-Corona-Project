package com.telekom.datacorona.region;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Transactional
public class RegionServiceJPA implements RegionService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addRegion(Region region) {
        try {
            entityManager
                    .createQuery("select r from Region r where r.id= :id")
                    .setParameter("id", region.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException | NoResultException iae) {
            entityManager.persist(region);
        }
    }

    @Override
    public List<Region> getAllRegions() {
        return entityManager
                .createQuery("select r from Region r")
                .getResultList();
    }
}
