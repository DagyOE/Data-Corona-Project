package com.telekom.datacorona.hospital;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class HospitalServiceJPA implements HospitalService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addHospital(Hospital hospital) {
        entityManager.persist(hospital);
    }

    @Override
    public List<Hospital> getAllHospitals() {
        return entityManager
                .createQuery("select h from Hospital h")
                .getResultList();
    }
}
