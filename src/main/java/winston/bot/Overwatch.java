package winston.bot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.PlayerNotFoundException;
import models.Hero.Hero;
import models.Player.Player;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import winston.bot.config.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Overwatch {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static Map<String, Hero> allHeroes = new HashMap<>();

    public Player getPlayerStats(List<String> args) throws Exception {

        String[] requestUrlInfo = determinePlatform(args);
        return getPlayerInformation(requestUrlInfo);
    }

    private Player getPlayerInformation(String[] requestUrlInfo) throws IOException, PlayerNotFoundException {

        Player player;
        String playerData = getDataFromAPI(requestUrlInfo[0]);

        player = objectMapper.readValue(playerData, Player.class);
        player.setOverbuffLink(requestUrlInfo[1]);

        return player;
    }

    private String[] determinePlatform(List<String> args) throws Exception {
        String url;
        String link;

        try {
            String platform = args.get(0).toLowerCase();
            if (platform.equalsIgnoreCase("pc")) {
                String[] battlenet = args.get(2).split("#");
                url = "https://overwatch-api.tekrop.fr/player/"
                        + battlenet[0] + "-" + battlenet[1]
                        + "/info?"
                        + "platform=" + platform
                        + "&region=" + args.get(1).toLowerCase();
                link = "https://www.overbuff.com/players/"
                        + platform + "/"
                        + battlenet[0] + "-" + battlenet[1];
            } else if (platform.equalsIgnoreCase("xbl") || platform.equalsIgnoreCase("psn")) {
                String user = args.get(1).toLowerCase();
                url = "https://overwatch-api.tekrop.fr/player/"
                        + user
                        + "/info?"
                        + "platform=" + platform;
                link = "https://www.overbuff.com/players/" + platform + "/" + user;
            } else {
                throw new PlayerNotFoundException("Arguments are Invalid / Wrong Order!");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PlayerNotFoundException("Player Not Found!");
        }

        return new String[]{url, link};
    }

    private String getDataFromAPI(String url) throws IOException, PlayerNotFoundException {

        HttpGet request = new HttpGet(url);
        request.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new PlayerNotFoundException("Player Not Found!");
        } else {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }

    void startup() throws IOException {

        allHeroes = objectMapper.readValue(getClass().getClassLoader().getResource("allHeroes.json"), new TypeReference<>() {
        });
        Logger.LOGGER.info("All Heroes Read Into Cache");

    }

    public static Map<String, Hero> getAllHeroes() {
        return allHeroes;
    }

}
