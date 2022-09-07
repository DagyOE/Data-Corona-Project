package com.telekom.datacorona.vaccine;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class VaccineServiceJPA implements VaccineService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addVaccine(Vaccine vaccine) {
        entityManager.persist(vaccine);
    }

    @Override
    public List<Vaccine> getAllVaccines() {
        return entityManager
                .createQuery("select v from Vaccine v")
                .getResultList();
    }
}
