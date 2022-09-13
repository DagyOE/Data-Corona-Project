package com.telekom.datacorona.slovakiaHospitalPatients;


import com.telekom.datacorona.regionVaccinations.RegionVaccinations;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class SlovakiaHospitalPatientsServiceJPA implements SlovakiaHospitalPatientsService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addSlovakiaHospitalPatients(SlovakiaHospitalPatients slovakiaHospitalPatients) {
        try {
            entityManager
                    .createQuery("select shp from SlovakiaHospitalPatients shp where shp.id= :id")
                    .setParameter("id", slovakiaHospitalPatients.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException iae) {
            entityManager.persist(slovakiaHospitalPatients);
        } catch (NoResultException nre) {
            entityManager.persist(slovakiaHospitalPatients);
        }
    }

    @Override
    public List<SlovakiaHospitalPatients> getAllSlovakiaHospitalPatients() {
        return entityManager
                .createQuery("select shp from SlovakiaHospitalPatients shp")
                .getResultList();
    }

    @Override
    public List<SlovakiaHospitalPatients> getDailyHospitalPatients(String from, String to) {
        return entityManager
                .createQuery("select shp from SlovakiaHospitalPatients shp where shp.publishedOn between :from and :to")
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
}
