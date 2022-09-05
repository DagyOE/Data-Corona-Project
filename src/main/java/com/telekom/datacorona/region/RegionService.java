package com.telekom.datacorona.region;

import java.util.List;

public interface RegionService {
    void addRegion(Region region);
    List<Region> getAllRegions();
}
