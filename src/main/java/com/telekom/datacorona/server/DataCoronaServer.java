package com.telekom.datacorona.server;

import com.telekom.datacorona.city.CityService;
import com.telekom.datacorona.city.CityServiceJPA;
import com.telekom.datacorona.district.DistrictService;
import com.telekom.datacorona.district.DistrictServiceJPA;
import com.telekom.datacorona.hospital.HospitalService;
import com.telekom.datacorona.hospital.HospitalServiceJPA;
import com.telekom.datacorona.region.RegionService;
import com.telekom.datacorona.region.RegionServiceJPA;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsService;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsServiceJPA;
import com.telekom.datacorona.slovakiaVaccinations.SlovakiaVaccinationsService;
import com.telekom.datacorona.slovakiaVaccinations.SlovakiaVaccinationsServiceJPA;
import com.telekom.datacorona.vaccinations.VaccinationsService;
import com.telekom.datacorona.vaccinations.VaccinationsServiceJPA;
import com.telekom.datacorona.vaccine.VaccineService;
import com.telekom.datacorona.vaccine.VaccineServiceJPA;
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
        "com.telekom.datacorona.regionVaccinations",
        "com.telekom.datacorona.slovakiaVaccinations",
        "com.telekom.datacorona.vaccine",
        "com.telekom.datacorona.vaccinations"
})
@ComponentScan(basePackages = {
        "com.telekom.datacorona.city",
        "com.telekom.datacorona.district",
        "com.telekom.datacorona.hospital",
        "com.telekom.datacorona.region",
        "com.telekom.datacorona.regionVaccinations",
        "com.telekom.datacorona.slovakiaVaccinations",
        "com.telekom.datacorona.vaccine",
        "com.telekom.datacorona.vaccinations"
})
public class DataCoronaServer {

    public static void main(String[] args) {
        SpringApplication.run(DataCoronaServer.class, args);
    }

    // ------> NEVIEM ALE SI TYMTO BY SME MOHLI OVLADAT ZAPIS DO DB
//    @Bean
    public CommandLineRunner runner(Console console) {
        return s -> console.run();
    }

//    @Bean
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
        return new RegionVaccinationsServiceJPA();
    }

    @Bean
    public SlovakiaVaccinationsService slovakiaVaccinationsService() {
        return new SlovakiaVaccinationsServiceJPA();
    }

    @Bean
    public VaccineService vaccineService() {
        return new VaccineServiceJPA();
    }

    @Bean
    public VaccinationsService vaccinationsService() {
        return new VaccinationsServiceJPA();
    }
}
