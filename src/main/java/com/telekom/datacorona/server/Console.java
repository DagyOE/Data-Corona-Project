package com.telekom.datacorona.server;

import com.telekom.datacorona.region.Region;
import com.telekom.datacorona.region.RegionService;

public class Console {

    private RegionService regionService;

    public void run() {
        System.err.println("App started");
        regionService.addRegion(new Region("Test title", "Test Code", "Test abbreviation"));
    }
}
