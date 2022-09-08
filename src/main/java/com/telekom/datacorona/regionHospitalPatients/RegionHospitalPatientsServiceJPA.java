package com.telekom.datacorona.regionHospitalPatients;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
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
        }
    }

    @Override
    public List<RegionHospitalPatients> getAllRegionHospitalPatients() {
        return entityManager
                .createQuery("select rhp from RegionHospitalPatients rhp")
                .getResultList();
    }
}
