package com.telekom.datacorona.slovakiaVaccinations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class SlovakiaVaccinationsServiceJPA implements SlovakiaVaccinationsService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addSlovakiaVaccinations(SlovakiaVaccinations slovakiaVaccinations) {
        entityManager.persist(slovakiaVaccinations);
    }

    @Override
    public List<SlovakiaVaccinations> getAllSlovakiaVaccinations() {
        return entityManager
                .createQuery("select sv from SlovakiaVaccinations sv")
                .getResultList();
    }
}
