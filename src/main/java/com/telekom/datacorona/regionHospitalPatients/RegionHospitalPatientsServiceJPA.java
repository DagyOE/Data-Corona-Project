package com.telekom.datacorona.regionHospitalPatients;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RegionHospitalPatientsServiceJPA implements RegionHospitalPatientsService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addRegionHospitalPatients(RegionHospitalPatients regionHospitalPatients) {
        try {
            entityManager
                    .createQuery("select rhp from RegionHospitalPatients rhp where rhp.id= :id")
                    .setParameter("id", regionHospitalPatients.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException iae) {
            entityManager.persist(regionHospitalPatients);
        } catch (NoResultException nre) {
            entityManager.persist(regionHospitalPatients);
        }
    }

    @Override
    public List<RegionHospitalPatients> getAllRegionHospitalPatients() {
        return entityManager
                .createQuery("select rhp from RegionHospitalPatients rhp")
                .getResultList();
    }

    @Override
    public List<RegionHospitalPatients> getCountRegionHospitalPatients(String from, String to) {
        return entityManager
                .createQuery("select rhp.region.title, sum(rhp.nonCovid), sum(rhp.confirmedCovid), sum(rhp.ventilatedCovid), sum(rhp.suspectedCovid) from RegionHospitalPatients rhp where rhp.publishedOn between :from and :to group by rhp.region.title")
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
}
