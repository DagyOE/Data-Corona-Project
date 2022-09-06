package com.telekom.datacorona.vaccinations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class VaccinationsServiceJPA  implements VaccinationsService{
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public void addVaccinations(Vaccinations vaccinations) {
        entityManager.persist(vaccinations);
    }

    @Override
    public List<Vaccinations> getAllVaccinations() {
        return entityManager
                .createQuery("select v from Vaccinations v")
                .getResultList();
    }
}
