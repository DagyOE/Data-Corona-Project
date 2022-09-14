package com.telekom.datacorona.district;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class DistrictServiceJPA implements DistrictService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addDistrict(District district) {
        try {
            entityManager
                    .createQuery("select d from District d where d.id= :id")
                    .setParameter("id", district.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException | NoResultException iae) {
            entityManager.persist(district);
        }
    }

    @Override
    public List<District> getAllDistricts() {
        return entityManager
                .createQuery("select d from District d")
                .getResultList();
    }
}
