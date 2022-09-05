package com.telekom.datacorona.server;

import com.telekom.datacorona.city.CityService;
import com.telekom.datacorona.city.CityServiceJPA;
import com.telekom.datacorona.district.DistrictService;
import com.telekom.datacorona.district.DistrictServiceJPA;
import com.telekom.datacorona.hospital.HospitalService;
import com.telekom.datacorona.hospital.HospitalServiceJPA;
import com.telekom.datacorona.region.RegionService;
import com.telekom.datacorona.region.RegionServiceJPA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.telekom.datacorona.city",
        "com.telekom.datacorona.district",
        "com.telekom.datacorona.hospital",
        "com.telekom.datacorona.region"
})
public class DataCoronaServer {

    public static void main(String[] args) {
        SpringApplication.run(DataCoronaServer.class, args);
    }

    @Bean
    public CityService cityService() {
        return new CityServiceJPA();
    }

    @Bean
    public DistrictService districtService() {
        return new DistrictServiceJPA();
    }

    @Bean
    public HospitalService hospitalService() {
        return new HospitalServiceJPA();
    }

    @Bean
    public RegionService regionService() {
        return new RegionServiceJPA();
    }
}
