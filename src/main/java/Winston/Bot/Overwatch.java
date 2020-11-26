package Winston.Bot;

import Exceptions.PlayerNotFoundException;
import Listeners.Listener;
import Models.Hero.Hero;
import Models.Player.Achievement.Achievements;
import Models.Player.Player;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Overwatch {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private static Map<String, Hero> allHeroes = new HashMap<>();

    public Player getPlayerStats(List<String> args) throws Exception {

        String[] requestUrlInfo = determinePlatform(args, "/info?");
        String[] requestUrlAchievements = determinePlatform(args, "/achievements?");

        Player player = getPlayerInformation(requestUrlInfo);
        getPlayerAchievements(requestUrlAchievements, player);

        return player;
    }

    private void getPlayerAchievements(String[] requestUrlAchievements, Player player) throws IOException, PlayerNotFoundException {

        String playerAchievementsData = getDataFromAPI(requestUrlAchievements[0]);
        player.setAchievements(objectMapper.readValue(playerAchievementsData, Achievements.class));

    }

    private Player getPlayerInformation(String[] requestUrlInfo) throws IOException, PlayerNotFoundException {

        Player player;
        String playerData = getDataFromAPI(requestUrlInfo[0]);

        player = objectMapper.readValue(playerData, Player.class);
        player.setOverbuffLink(requestUrlInfo[1]);

        return player;
    }

    private String[] determinePlatform(List<String> args, String requestType) throws Exception {
        String url;
        String link;

        try {
            String platform = args.get(0);
            if (platform.equalsIgnoreCase("pc")) {
                String[] battlenet = args.get(2).split("#");
                url = "https://overwatch-api.tekrop.fr/player/"
                        + battlenet[0] + "-" + battlenet[1]
                        + requestType
                        + "platform=" + platform
                        + "&region=" + args.get(1);
                link = "https://www.overbuff.com/players/"
                        + platform + "/"
                        + battlenet[0] + "-" + battlenet[1];
            } else {
                String user = args.get(1);
                url = "https://overwatch-api.tekrop.fr/player/"
                        + user
                        + requestType
                        + "platform=" + platform;
                link = "https://www.overbuff.com/players/" + platform + "/" + user;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PlayerNotFoundException("Arguments are Invalid / Wrong Order!");
        } catch (Exception e) {
            throw new Exception("Unknown Exception Occurred" + e);
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
        allHeroes = objectMapper.readValue(new File("/src/main/resources/allHeroes.json"), new TypeReference<>() {
        });
        LOGGER.info("All Heroes Read Into Cache");

    }

    public static Map<String, Hero> getAllHeroes() {
        return allHeroes;
    }

}
