package Listeners;

import Models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Overwatch {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void getPlayerStats() throws IOException {

//        URL url = new URL("https://owapi.io/profile/pc/eu/Hammy-21436");
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.connect();
//
//        //Getting the response code
//        int responsecode = conn.getResponseCode();

//        URL url = new URL("https://ow-api.com/v1/stats/pc/eu/Hammy-21436/profile/");
//        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
//        uc.setRequestMethod("GET");
//        uc.setRequestProperty("Content-Type", "application/json");
//        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0");
//
//        int response = uc.getResponseCode();

        Player value = objectMapper.readValue(new File("test.json"), Player.class);
        System.out.println(value.getIcon());
    }
}
