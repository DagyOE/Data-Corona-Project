package com.telekom.datacorona.district;

import java.util.List;

public interface DistrictService {
    void addDistrict(District district);
    List<District> getAllDistricts();
}
