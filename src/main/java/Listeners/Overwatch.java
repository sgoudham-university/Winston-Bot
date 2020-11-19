package Listeners;

import Models.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Overwatch {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public Player getPlayerStats() throws IOException {

        HttpGet request = new HttpGet("https://ow-api.com/v1/stats/pc/eu/Hammy-21436/profile");
        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);

        return objectMapper.readValue(result, Player.class);
    }
}
