package com.telekom.datacorona.server;

import java.io.*;
import java.net.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telekom.datacorona.city.City;
import com.telekom.datacorona.city.CityService;
import com.telekom.datacorona.district.District;
import com.telekom.datacorona.district.DistrictService;
import com.telekom.datacorona.hospital.Hospital;
import com.telekom.datacorona.hospital.HospitalService;
import com.telekom.datacorona.region.Region;
import com.telekom.datacorona.region.RegionService;
import com.telekom.datacorona.regionVaccinations.RegionVaccinations;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsService;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Console {

    @Autowired
    private RegionService regionService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CityService cityService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private RegionVaccinationsService regionVaccinationsService;

    public void run() throws IOException {
        System.out.println("App started");
        getRegionData("https://data.korona.gov.sk/api/regions");
        getDistrictData("https://data.korona.gov.sk/api/districts");
        getCityData("https://data.korona.gov.sk/api/cities");
        getHospitalData("https://data.korona.gov.sk/api/hospitals");
    }

    private void getHospitalData(String url) throws IOException {
        String data = jsonString(url);
        ObjectMapper mapper = new ObjectMapper();
        Hospital[] pp1 = mapper.readValue(data, Hospital[].class);

        System.out.println("JSON array to Array objects...");
        for (Hospital hospital : pp1) {
            System.out.println(hospital);
            hospitalService.addHospital(new Hospital(hospital.getCity(), hospital.getTitle(), hospital.getCode()));
        }
    }

    private void getCityData(String url) throws IOException {
        String data = jsonString(url);
        ObjectMapper mapper = new ObjectMapper();
        City[] pp1 = mapper.readValue(data, City[].class);

        System.out.println("JSON array to Array objects...");
        for (City city : pp1) {
            System.out.println(city);
            cityService.addCity(new City(city.getDistrict(), city.getCode(), city.getTitle()));
        }
    }

    private void getDistrictData(String url) throws IOException {
        String data = jsonString(url);
        ObjectMapper mapper = new ObjectMapper();
        District[] pp1 = mapper.readValue(data, District[].class);

        System.out.println("JSON array to Array objects...");
        for (District district : pp1) {
            System.out.println(district);
            districtService.addDistrict(new District(district.getRegionId(), district.getTitle(), district.getCode()));
        }
    }

    private void getRegionData(String url) throws IOException {
        String data = jsonString(url);
        ObjectMapper mapper = new ObjectMapper();
        Region[] pp1 = mapper.readValue(data, Region[].class);

        System.out.println("JSON array to Array objects...");
        for (Region region : pp1) {
            System.out.println(region);
            regionService.addRegion(new Region(region.getTitle(), region.getCode(), region.getAbbreviation()));
        }
    }

    private String jsonString(String urlString) throws IOException {
        String data = "";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        System.err.println("Response code: " + responseCode);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                data+=line;
            }
        }
        String prop = "id";
        data = data.replaceAll("\"" + prop + "\"[ ]*:[^,}\\]]*[,]?", "");
        data = data.replaceAll("region_id", "regionId");
        data = data.replaceAll("district_id", "district");
        data = data.replaceAll("city_id", "city");
        System.out.println(data);
        return data;
    }
}
