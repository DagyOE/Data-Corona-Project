package com.telekom.datacorona.server;

import com.telekom.datacorona.region.Region;
import com.telekom.datacorona.region.RegionService;
import com.telekom.datacorona.regionVaccinations.RegionVaccinations;
import com.telekom.datacorona.regionVaccinations.RegionVaccinationsService;
import com.telekom.datacorona.slovakiaVaccinations.SlovakiaVaccinations;
import com.telekom.datacorona.slovakiaVaccinations.SlovakiaVaccinationsService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Console {

    @Autowired
    private SlovakiaVaccinationsService slovakiaVaccinationsService;

    public void run() {
        System.out.println("App started");
        try {
            URL url = new URL("https://data.korona.gov.sk/api/regions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            System.err.println("Response code: " + responseCode);
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();
                JSONParser parser = new JSONParser();
                JSONObject dataObj = (JSONObject) parser.parse(inline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
