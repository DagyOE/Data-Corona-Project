package com.telekom.datacorona.vaccinations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class VaccinationsServiceJPA implements VaccinationsService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addVaccination(Vaccinations vaccinations) {
        try {
            entityManager
                    .createQuery("select v from Vaccinations v where v.id= :id")
                    .setParameter("id", vaccinations.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException iae) {
            entityManager.persist(vaccinations);
        }
    }

    @Override
    public List<Vaccinations> getAllVaccinations() {
        return entityManager
                .createQuery("select v from Vaccinations v")
                .getResultList();
    }
}
