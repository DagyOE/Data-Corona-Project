package com.telekom.datacorona.city;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CityServiceJPA implements CityService{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addCity(City city) {
        entityManager.persist(city);
    }

    @Override
    public List<City> getAllCities() {
        return entityManager
                .createQuery("select c from City c")
                .getResultList();
    }
}
