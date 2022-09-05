package com.telekom.datacorona.city;

import java.util.List;

public interface CityService {
    void addCity(City city);
    List<City> getAllCities();
}
