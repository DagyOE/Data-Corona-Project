package com.telekom.datacorona.city;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CityServiceJPA implements CityService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addCity(City city) {
        try {
            entityManager
                    .createQuery("select c from City c where c.id= :id")
                    .setParameter("id", city.getId())
                    .getSingleResult();
        } catch (IllegalArgumentException | NoResultException iae) {
            entityManager.persist(city);
        }
    }

    @Override
    public List<City> getAllCities() {
        return entityManager
                .createQuery("select c from City c")
                .getResultList();
    }
}
