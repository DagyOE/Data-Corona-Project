package com.telekom.datacorona.slovakiaVaccinations;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class SlovakiaVaccinationsServiceJPA implements SlovakiaVaccinationsService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addSlovakiaVaccinations(SlovakiaVaccinations slovakiaVaccinations) {
        try {
            entityManager
                    .createQuery("select sv from SlovakiaVaccinations sv where sv.id= :id")
                    .setParameter("id", slovakiaVaccinations.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException iae) {
            entityManager.persist(slovakiaVaccinations);
        } catch (NoResultException nre) {
            entityManager.persist(slovakiaVaccinations);
        }
    }

    @Override
    public List<SlovakiaVaccinations> getAllSlovakiaVaccinations() {
        return entityManager
                .createQuery("select sv from SlovakiaVaccinations sv")
                .getResultList();
    }
}
