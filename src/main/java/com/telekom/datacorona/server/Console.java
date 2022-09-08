package com.telekom.datacorona.server;

import java.io.*;
import java.net.*;

import com.telekom.datacorona.city.City;
import com.telekom.datacorona.city.CityService;
import com.telekom.datacorona.district.District;
import com.telekom.datacorona.district.DistrictService;
import com.telekom.datacorona.hospital.Hospital;
import com.telekom.datacorona.hospital.HospitalService;
import com.telekom.datacorona.region.Region;
import com.telekom.datacorona.region.RegionService;
import com.telekom.datacorona.regionHospitalPatients.RegionHospitalPatients;
import com.telekom.datacorona.regionHospitalPatients.RegionHospitalPatientsService;
import com.telekom.datacorona.regionVaccinations.RegionVaccinations;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsService;
import com.telekom.datacorona.slovakiaVaccinations.SlovakiaVaccinations;
import com.telekom.datacorona.slovakiaVaccinations.SlovakiaVaccinationsService;
import com.telekom.datacorona.vaccinations.Vaccinations;
import com.telekom.datacorona.vaccinations.VaccinationsService;
import com.telekom.datacorona.vaccine.Vaccine;
import com.telekom.datacorona.vaccine.VaccineService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
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
    @Autowired
    private SlovakiaVaccinationsService slovakiaVaccinationsService;
    @Autowired
    private VaccineService vaccineService;
    @Autowired
    private VaccinationsService vaccinationsService;
    @Autowired
    private RegionHospitalPatientsService regionHospitalPatientsService;

    private String urlRegion = "https://data.korona.gov.sk/api/regions";
    private String urlDistrict = "https://data.korona.gov.sk/api/districts";
    private String urlCity = "https://data.korona.gov.sk/api/cities";
    private String urlHospital = "https://data.korona.gov.sk/api/hospitals";
    private String urlVaccinationsByRegion = "https://data.korona.gov.sk/api/vaccinations/by-region";
    private String urlVaccinationsInSlovakia = "https://data.korona.gov.sk/api/vaccinations/in-slovakia";
    private String urlVaccine = "https://data.korona.gov.sk/api/vaccines";
    private String urlVaccination = "https://data.korona.gov.sk/api/vaccinations";
    private String urlRegionHospitalPatients = "https://data.korona.gov.sk/api/hospital-patients/by-region";

    @Scheduled(fixedRate = 86400000)
    public void run() throws IOException, JSONException {
        System.out.println("Start mirroring");
        getRegionData(urlRegion);
        getDistrictData(urlDistrict);
        getCityData(urlCity);
        getHospitalData(urlHospital);
        getVaccinationByRegionData(urlVaccinationsByRegion);
        getVaccinationInSlovakiaData(urlVaccinationsInSlovakia);
        getVaccineData(urlVaccine);
        getVaccinationData(urlVaccination);
        getRegionHospitalPatientsData(urlRegionHospitalPatients);
        System.out.println("End mirroring");
    }

    private void getRegionHospitalPatientsData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONObject obj = new JSONObject(jsonString);
        JSONArray data = obj.getJSONArray("page");
        for (int i = 0; i < data.length(); i++) {
            regionHospitalPatientsService.addRegionHospitalPatients(new RegionHospitalPatients(
                    data.getJSONObject(i).get("id").toString(),
                    (String) data.getJSONObject(i).get("oldest_reported_at"),
                    (String) data.getJSONObject(i).get("newest_reported_at"),
                    new Region((int) data.getJSONObject(i).get("region_id")),
                    (int) data.getJSONObject(i).get("ventilated_covid"),
                    (int) data.getJSONObject(i).get("non_covid"),
                    (int) data.getJSONObject(i).get("confirmed_covid"),
                    (int) data.getJSONObject(i).get("suspected_covid"),
                    (String) data.getJSONObject(i).get("published_on"),
                    (String) data.getJSONObject(i).get("updated_at")
            ));
        }
        if (!(obj.get("next_offset").toString().equals("null"))) {
            String newUrl = urlRegionHospitalPatients + "?offset=" + obj.get("next_offset");
            getRegionHospitalPatientsData(newUrl);
        }
    }

    private void getVaccinationData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONObject obj = new JSONObject(jsonString);
        JSONArray data = obj.getJSONArray("page");
        for (int i = 0; i < data.length(); i++) {
            vaccinationsService.addVaccination(new Vaccinations(
                    (String) data.getJSONObject(i).get("id"),
                    new Vaccine((int) data.getJSONObject(i).get("vaccine_id")),
                    new Region((int) data.getJSONObject(i).get("region_id")),
                    (int) data.getJSONObject(i).get("dose1_count"),
                    (int) data.getJSONObject(i).get("dose2_count"),
                    (String) data.getJSONObject(i).get("updated_at"),
                    (String) data.getJSONObject(i).get("published_on")
            ));
        }
        if (!(obj.get("next_offset").toString().equals("null"))) {
            String newUrl = urlVaccination + "?offset=" + obj.get("next_offset");
            getVaccinationData(newUrl);
        }
    }

    private void getVaccineData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONArray pageName = new JSONArray(jsonString);
        for (int i = 0; i < pageName.length(); i++) {
            vaccineService.addVaccine(new Vaccine(
                    (int) pageName.getJSONObject(i).get("id"),
                    (String) pageName.getJSONObject(i).get("title"),
                    (String) pageName.getJSONObject(i).get("manufacturer")
                    ));
        }
    }

    private void getVaccinationInSlovakiaData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONObject obj = new JSONObject(jsonString);
        JSONArray data = obj.getJSONArray("page");
        for (int i = 0; i < data.length(); i++) {
            slovakiaVaccinationsService.addSlovakiaVaccinations(new SlovakiaVaccinations(
                    (String) data.getJSONObject(i).get("id"),
                    (int) data.getJSONObject(i).get("dose1_count"),
                    (int) data.getJSONObject(i).get("dose2_count"),
                    (int) data.getJSONObject(i).get("dose1_sum"),
                    (int) data.getJSONObject(i).get("dose2_sum"),
                    (String) data.getJSONObject(i).get("updated_at"),
                    (String) data.getJSONObject(i).get("published_on")
            ));
        }
        if (!(obj.get("next_offset").toString().equals("null"))) {
            String newUrl = urlVaccinationsInSlovakia + "?offset=" + obj.get("next_offset");
            getVaccinationInSlovakiaData(newUrl);
        }
    }

    private void getVaccinationByRegionData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONObject obj = new JSONObject(jsonString);
        JSONArray data = obj.getJSONArray("page");
        for (int i = 0; i < data.length(); i++) {
            regionVaccinationsService.addRegionVaccinations(new RegionVaccinations(
                    (String) data.getJSONObject(i).get("id"),
                    new Region((int) data.getJSONObject(i).get("region_id")),
                    (int) data.getJSONObject(i).get("dose1_count"),
                    (int) data.getJSONObject(i).get("dose2_count"),
                    (int) data.getJSONObject(i).get("dose1_sum"),
                    (int) data.getJSONObject(i).get("dose2_sum"),
                    (String) data.getJSONObject(i).get("updated_at"),
                    (String) data.getJSONObject(i).get("published_on")
            ));
        }
        if (!(obj.get("next_offset").toString().equals("null"))) {
            String newUrl = urlVaccinationsByRegion + "?offset=" + obj.get("next_offset");
            getVaccinationByRegionData(newUrl);
        }
    }

    private void getHospitalData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONArray data = new JSONArray(jsonString);
        for (int i = 0; i < data.length(); i++) {
            hospitalService.addHospital(new Hospital(
                    (int) data.getJSONObject(i).get("id"),
                    new City((int) data.getJSONObject(i).get("city_id")),
                    (String) data.getJSONObject(i).get("title"),
                    (String) data.getJSONObject(i).get("code")
            ));
        }
    }

    private void getCityData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONArray data = new JSONArray(jsonString);
        for (int i = 0; i < data.length(); i++) {
            cityService.addCity(new City(
                    (int) data.getJSONObject(i).get("id"),
                    new District((int) data.getJSONObject(i).get("district_id")),
                    (String) data.getJSONObject(i).get("title"),
                    (String) data.getJSONObject(i).get("code")
            ));
        }
    }

    private void getDistrictData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONArray data = new JSONArray(jsonString);
        for (int i = 0; i < data.length(); i++) {
            districtService.addDistrict(new District(
                    (int) data.getJSONObject(i).get("id"),
                    new Region((int) data.getJSONObject(i).get("region_id")),
                    (String) data.getJSONObject(i).get("title"),
                    (String) data.getJSONObject(i).get("code")
            ));
        }
    }

    private void getRegionData(String url) throws IOException, JSONException {
        String jsonString = jsonString(url);
        JSONArray data = new JSONArray(jsonString);
        for (int i = 0; i < data.length(); i++) {
            regionService.addRegion(new Region(
                    (int) data.getJSONObject(i).get("id"),
                    (String) data.getJSONObject(i).get("title"),
                    (String) data.getJSONObject(i).get("code"),
                    (String) data.getJSONObject(i).get("abbreviation")
            ));
        }
    }

    private String jsonString(String urlString) throws IOException {
        String data = "";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                data += line;
            }
        }
        return data;
    }
}
