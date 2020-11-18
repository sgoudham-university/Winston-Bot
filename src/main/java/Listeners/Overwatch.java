package Listeners;

import Models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Overwatch {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void getPlayerStats() throws IOException {

        URL url = new URL("https://owapi.io/profile/pc/eu/Hammy-21436");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        //Getting the response code
        int responsecode = conn.getResponseCode();

        URL url2 = new URL("https://ow-api.com/v1/stats/pc/eu/Hammy-21436/profile");
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setRequestMethod("GET");
        uc.addRequestProperty("User-Agent", "Mozilla");

        Player value = objectMapper.readValue(url, Player.class);
        System.out.println(value);
    }
}
