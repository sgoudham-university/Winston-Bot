package Winston.Bot;

import Exceptions.PlayerNotFoundException;
import Models.Hero.Ability;
import Models.Hero.Hero;
import Models.Player.Achievement.Achievements;
import Models.Player.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Overwatch {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private Map<String, Hero> allHeroes = new HashMap<>();

    public Player getPlayerStats(List<String> args) throws Exception {

        String[] requestUrlInfo = determinePlatform(args, "/info?");
        String[] requestUrlAchievements = determinePlatform(args, "/achievements?");

        Player player = getPlayerInformation(requestUrlInfo);
        getPlayerAchievements(requestUrlAchievements, player);

        return player;
    }

    private void getPlayerAchievements(String[] requestUrlAchievements, Player player) throws IOException {

        HttpGet request = new HttpGet(requestUrlAchievements[0]);
        request.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);

        player.setAchievements(objectMapper.readValue(result, Achievements.class));

    }

    private Player getPlayerInformation(String[] requestUrlInfo) throws IOException, PlayerNotFoundException {
        Player player;

        HttpGet request = new HttpGet(requestUrlInfo[0]);
        request.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);

        if (response.getStatusLine().getStatusCode() == 200) {
            player = objectMapper.readValue(result, Player.class);
            player.setOverbuffLink(requestUrlInfo[1]);
        } else {
            throw new PlayerNotFoundException("Player Not Found");
        }

        return player;
    }

    private static String[] determinePlatform(List<String> args, String requestType) throws PlayerNotFoundException {
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
        }


        return new String[]{url, link};
    }

    public Hero getHero(List<String> args) throws IOException {

        HttpGet request = new HttpGet("https://overwatch-api.tekrop.fr/hero/" + args.get(0));
        request.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);

        Hero hero = objectMapper.readValue(result, Hero.class);

        hero.setPortraitUrl("https://d1u1mce87gyfbn.cloudfront.net/hero/"
                + hero.getName().toLowerCase().replace(" ", "-")
                + "/hero-select-portrait.png");

        for (Ability ability : hero.getAbilities()) {
            ability.setIcon("https://d1u1mce87gyfbn.cloudfront.net/hero/"
                    + hero.getName().toLowerCase().replace(" ", "-") + "/ability-"
                    + ability.getName().toLowerCase().replace(" ", "-")
                    + "/icon-ability.png");
        }

        return hero;
    }

    public void startup() {
    }
}
