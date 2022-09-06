package com.telekom.datacorona.server;

import com.telekom.datacorona.city.CityService;
import com.telekom.datacorona.city.CityServiceJPA;
import com.telekom.datacorona.district.DistrictService;
import com.telekom.datacorona.district.DistrictServiceJPA;
import com.telekom.datacorona.hospital.HospitalService;
import com.telekom.datacorona.hospital.HospitalServiceJPA;
import com.telekom.datacorona.region.RegionService;
import com.telekom.datacorona.region.RegionServiceJPA;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsJPA;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.telekom.datacorona.city",
        "com.telekom.datacorona.district",
        "com.telekom.datacorona.hospital",
        "com.telekom.datacorona.region",
        "com.telekom.datacorona.regionVaccinations"
})
@ComponentScan(basePackages = {
        "com.telekom.datacorona.city",
        "com.telekom.datacorona.district",
        "com.telekom.datacorona.hospital",
        "com.telekom.datacorona.region",
        "com.telekom.datacorona.regionVaccinations"
})
public class DataCoronaServer {

    public static void main(String[] args) {
        SpringApplication.run(DataCoronaServer.class, args);
    }

    // ------> NEVIEM ALE SI TYMTO BY SME MOHLI OVLADAT ZAPIS DO DB
    @Bean
    public CommandLineRunner runner(Console console) {
        return s -> console.run();
    }

    @Bean
    public Console console() {
        return new Console();
    }
    // ------>

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

    @Bean
    public RegionVaccinationsService regionVaccinationsService() {
        return new RegionVaccinationsJPA();
    }
}
